package Ideias.dto;


public record UtilizadorDto(
	    Long id,
	    String email,
	    String nome,
	    String password,
	    String tipo
	) {}