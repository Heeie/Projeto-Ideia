package Ideias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GrupoCriarDto(@NotBlank @Size(max=80) String nome,
        @NotBlank @Size(max=500) String descricao, @NotNull Long criadorId) {}
