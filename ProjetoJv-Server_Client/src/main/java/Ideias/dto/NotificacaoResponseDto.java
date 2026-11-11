package Ideias.dto;

import java.time.LocalDateTime;

public record NotificacaoResponseDto(Long id, String tipo, String texto, String link,
        boolean lida, LocalDateTime criadoEm) {}
