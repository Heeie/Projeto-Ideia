package Ideias.dto;

import Ideias.enums.CategoriaIdeia;
import Ideias.enums.IdeiaStatus;

public record IdeiaResponseDto(Long id, String nome, String descricao, String imagem,
        CategoriaIdeia categoria, IdeiaStatus estado, Long clienteId, String clienteNome) {}
