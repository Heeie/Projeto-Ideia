package main.Ideias.dto;


public record UtilizadorDto(
    Long id,
    String email,
    String nome,
    String tipo  // CLIENTE ou ADMIN
) {}
