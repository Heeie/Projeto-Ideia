package Ideias.dto;

import java.time.LocalDateTime;

public record MensagemResponseDto(Long id, Long remetenteId, String remetenteNome,
        String texto, LocalDateTime criadoEm) {}
