package Ideias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComentarioCriarDto(@NotNull Long autorId, @NotBlank @Size(max = 2000) String texto) {}
