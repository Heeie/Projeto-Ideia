package main.Ideias.dto;

import main.Ideias.enums.CategoriaIdeia;
import main.Ideias.entities.Cliente;

public record IdeiaDto(
    Long id,
    String nome,
    String descricao,
    CategoriaIdeia categoria,
    Cliente cliente,
) {}

