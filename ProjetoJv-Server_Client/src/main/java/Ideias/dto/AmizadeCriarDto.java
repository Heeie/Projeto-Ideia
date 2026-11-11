package Ideias.dto;

import jakarta.validation.constraints.NotNull;

public record AmizadeCriarDto(@NotNull Long requerenteId, @NotNull Long destinatarioId) {}
