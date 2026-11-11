package Ideias.dto;

import Ideias.enums.CategoriaIdeia;
import Ideias.enums.IdeiaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IdeiaDto(
        Long id,
        @NotBlank @Size(max = 120) String nome,
        @Size(max = 5000)
        String descricao,
        @Size(max = 3000000)
        String imagem,
        @NotNull
        CategoriaIdeia categoria,
        @NotNull
        IdeiaStatus estado,
        @NotNull
        Long clienteId
) {}

