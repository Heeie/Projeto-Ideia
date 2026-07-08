package Ideias.dto;

import Ideias.enums.CategoriaIdeia;
import Ideias.enums.IdeiaStatus;

public record IdeiaDto(
        Long id,
        String nome,
        String descricao,
        CategoriaIdeia categoria,
        IdeiaStatus estado,
        Long clienteId
) {}

