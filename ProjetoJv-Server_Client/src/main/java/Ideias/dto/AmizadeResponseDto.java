package Ideias.dto;

import java.time.LocalDateTime;

public record AmizadeResponseDto(Long id, Long requerenteId, String requerenteNome,
        Long destinatarioId, String destinatarioNome, String estado, LocalDateTime criadoEm) {}
