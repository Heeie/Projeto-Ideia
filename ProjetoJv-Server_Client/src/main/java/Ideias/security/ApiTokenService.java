package Ideias.security;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class ApiTokenService {
    public static final Duration TOKEN_LIFETIME = Duration.ofHours(12);

    private record Session(Long userId, Instant expiresAt) {}

    private final SecureRandom random = new SecureRandom();
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public String issue(Long userId) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sessions.put(token, new Session(userId, Instant.now().plus(TOKEN_LIFETIME)));
        return token;
    }

    public Long userId(String token) {
        if (token == null) return null;
        Session session = sessions.get(token);
        if (session == null) return null;
        if (session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(token, session);
            return null;
        }
        return session.userId();
    }

    public void revoke(String token) {
        if (token != null) sessions.remove(token);
    }

    public void revokeUser(Long userId) {
        sessions.entrySet().removeIf(entry -> entry.getValue().userId().equals(userId));
    }

    public Instant expiresAt() {
        return Instant.now().plus(TOKEN_LIFETIME);
    }
}
