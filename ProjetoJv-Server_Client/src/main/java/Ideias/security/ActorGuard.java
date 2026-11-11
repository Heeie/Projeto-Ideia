package Ideias.security;

import Ideias.exceptions.ForbiddenException;
import Ideias.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;

public final class ActorGuard {
    private ActorGuard() {}

    public static Long currentUserId(HttpServletRequest request) {
        Object value = request.getAttribute(ApiAuthenticationFilter.USER_ID_ATTRIBUTE);
        if (!(value instanceof Long userId)) throw new UnauthorizedException("Inicia sessão para continuar.");
        return userId;
    }

    public static void requireCurrentUser(HttpServletRequest request, Long requestedUserId) {
        if (!currentUserId(request).equals(requestedUserId))
            throw new ForbiddenException("Não podes agir em nome de outra conta.");
    }
}
