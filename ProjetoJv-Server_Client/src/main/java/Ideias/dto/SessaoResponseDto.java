package Ideias.dto;

import java.time.Instant;

public record SessaoResponseDto(Long id, String email, String nome, String tipo,
        String accessToken, String tokenType, Instant expiresAt) {}
