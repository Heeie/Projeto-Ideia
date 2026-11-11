package Ideias.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class ApiAuthenticationFilter extends OncePerRequestFilter {
    public static final String USER_ID_ATTRIBUTE = ApiAuthenticationFilter.class.getName() + ".userId";
    private static final int AUTH_LIMIT = 20;
    private static final long AUTH_WINDOW_MILLIS = 15 * 60 * 1000L;

    private record Window(long startedAt, int attempts) {}

    private final ApiTokenService tokens;
    private final Map<String, Window> authAttempts = new ConcurrentHashMap<>();

    public ApiAuthenticationFilter(ApiTokenService tokens) {
        this.tokens = tokens;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        addSecurityHeaders(response);
        if (!request.getRequestURI().startsWith(request.getContextPath() + "/api/")) {
            chain.doFilter(request, response);
            return;
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String token = bearerToken(request);
        Long userId = token == null ? null : tokens.userId(token);
        if (token != null && userId == null) {
            error(response, HttpServletResponse.SC_UNAUTHORIZED, "A sessão expirou. Inicia sessão novamente.");
            return;
        }
        if (isAuthenticationRequest(request)) {
            if (isRateLimited(request)) {
                response.setHeader("Retry-After", "900");
                error(response, 429, "Demasiadas tentativas. Aguarda antes de tentar novamente.");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        if (isPublicIdeaRead(request)) {
            if (userId != null) request.setAttribute(USER_ID_ATTRIBUTE, userId);
            chain.doFilter(request, response);
            return;
        }
        if (userId == null) {
            error(response, HttpServletResponse.SC_UNAUTHORIZED, "Inicia sessão para continuar.");
            return;
        }
        request.setAttribute(USER_ID_ATTRIBUTE, userId);
        chain.doFilter(request, response);
    }

    private boolean isAuthenticationRequest(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) return false;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return "/api/auth/login".equals(path) || "/api/auth/registo".equals(path);
    }

    private boolean isPublicIdeaRead(HttpServletRequest request) {
        if (!"GET".equalsIgnoreCase(request.getMethod())) return false;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return ("/api/ideias".equals(path) || path.matches("/api/ideias/[0-9]+"))
                && request.getQueryString() == null;
    }

    private boolean isRateLimited(HttpServletRequest request) {
        String key = request.getRemoteAddr() == null ? "unknown" : request.getRemoteAddr();
        long now = System.currentTimeMillis();
        Window next = authAttempts.compute(key, (ignored, previous) -> {
            if (previous == null || now - previous.startedAt() >= AUTH_WINDOW_MILLIS)
                return new Window(now, 1);
            return new Window(previous.startedAt(), previous.attempts() + 1);
        });
        if (authAttempts.size() > 4096) authAttempts.entrySet().removeIf(entry -> now - entry.getValue().startedAt() >= AUTH_WINDOW_MILLIS);
        return next.attempts() > AUTH_LIMIT;
    }

    private String bearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.regionMatches(true, 0, "Bearer ", 0, 7)) return null;
        String value = header.substring(7).trim();
        return value.isEmpty() ? null : value;
    }

    private void addSecurityHeaders(HttpServletResponse response) {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        response.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
        response.setHeader("Cache-Control", "no-store");
    }

    private void error(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"timestamp\":\"" + Instant.now() + "\",\"status\":" + status
                + ",\"message\":\"" + message + "\"}");
    }
}
