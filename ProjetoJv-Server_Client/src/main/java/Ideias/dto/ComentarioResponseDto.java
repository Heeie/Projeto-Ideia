package Ideias.dto;

import java.time.LocalDateTime;

public record ComentarioResponseDto(Long id, String texto, Long autorId, String autorNome,
        int gostos, boolean gostadoPorMim, LocalDateTime criadoEm) {}
