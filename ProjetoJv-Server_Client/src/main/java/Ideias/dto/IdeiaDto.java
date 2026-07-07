package Ideias.dto;

import Ideias.enums.CategoriaIdeia;
import Ideias.entities.Cliente;

public record IdeiaDto(
    Long id,
    String nome,
    String descricao,
    CategoriaIdeia categoria,
    Cliente cliente
) {}

