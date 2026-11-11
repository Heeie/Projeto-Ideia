package Ideias.dto;

import jakarta.validation.constraints.NotNull;

public record ConversaCriarDto(@NotNull Long utilizadorId, @NotNull Long contactoId) {}
