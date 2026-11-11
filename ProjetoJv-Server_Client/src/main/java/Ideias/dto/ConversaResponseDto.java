package Ideias.dto;

import java.time.LocalDateTime;

public record ConversaResponseDto(Long id, Long contactoId, String contactoNome,
        String ultimaMensagem, LocalDateTime atualizadaEm) {}
