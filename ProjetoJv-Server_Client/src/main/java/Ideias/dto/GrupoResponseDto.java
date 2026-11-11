package Ideias.dto;

import java.util.List;

public record GrupoResponseDto(Long id, String nome, String descricao, Long criadorId,
        String criadorNome, List<Long> membros, boolean membroAtual) {}
