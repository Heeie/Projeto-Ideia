package Ideias.mapper;

import java.util.List;
import java.util.stream.Collectors;

import Ideias.dto.IdeiaDto;
import Ideias.dto.UtilizadorDto;
import Ideias.entities.Admin;
import Ideias.entities.Cliente;
import Ideias.entities.Ideia;
import Ideias.entities.Utilizador;

public class Mapper {

    // ========================================
    // ============ UTILIZADOR -> DTO ==========
    // ========================================

    public static UtilizadorDto mapToUtilizadorDto(Utilizador utilizador) {

        if (utilizador == null)
            return null;

        String tipo = utilizador instanceof Cliente ? "CLIENTE" : "ADMIN";

        return new UtilizadorDto(
                utilizador.getId(),
                utilizador.getEmail(),
                utilizador.getNome(),
                utilizador.getPassword(),
                tipo
        );
    }

    public static List<UtilizadorDto> mapToUtilizadorDtoList(List<Utilizador> utilizadores) {
        return utilizadores.stream()
                .map(Mapper::mapToUtilizadorDto)
                .collect(Collectors.toList());
    }

    // ========================================
    // ============ DTO -> UTILIZADOR ==========
    // ========================================

    public static Utilizador mapDtoToUtilizador(UtilizadorDto dto) {

        if (dto == null)
            return null;

        switch (dto.tipo()) {

            case "CLIENTE":
                return new Cliente(
                        dto.email(),
                        dto.nome(),
                        dto.password()
                );

            case "ADMIN":
                return new Admin(
                        dto.email(),
                        dto.nome(),
                        dto.password()
                );

            default:
                throw new IllegalArgumentException("Tipo de utilizador inválido.");
        }
    }

    // ========================================
    // ============== IDEIA -> DTO ============
    // ========================================

    public static IdeiaDto mapToIdeiaDto(Ideia ideia) {

        if (ideia == null)
            return null;

        return new IdeiaDto(
        	    ideia.getId(),
        	    ideia.getNome(),
        	    ideia.getDescricao(),
        	    ideia.getCategoria(),
        	    ideia.getEstado(),
        	    ideia.getCliente() != null ? ideia.getCliente().getId() : null
        	);
    }

    public static List<IdeiaDto> mapToIdeiaDtoList(List<Ideia> ideias) {
        return ideias.stream()
                .map(Mapper::mapToIdeiaDto)
                .collect(Collectors.toList());
    }

    // ========================================
    // ============== DTO -> IDEIA ============
    // ========================================

    public static Ideia mapDtoToIdeia(IdeiaDto dto) {

        if (dto == null)
            return null;

        Ideia ideia = new Ideia();

        ideia.setNome(dto.nome());
        ideia.setDescricao(dto.descricao());
        ideia.setCategoria(dto.categoria());
        ideia.setEstado(dto.estado());

        // O cliente será atribuído no Service

        return ideia;
    }

}