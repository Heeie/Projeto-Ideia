package Ideias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MensagemCriarDto(@NotNull Long remetenteId, @NotBlank @Size(max=4000) String texto) {}
