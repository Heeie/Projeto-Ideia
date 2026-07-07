package Ideias.mapper;

import java.util.List;
import java.util.stream.Collectors;

import Ideias.dto.*;
import Ideias.entities.*;
import Ideias.enums.*;

public class Mapper {

    // ========================================
    // ============ ENTIDADE -> DTO ============
    // ========================================
    

    public static UtilizadorDto mapToUtilizadorDto(Utilizador utilizador) {
        if (utilizador == null) return null;

        String tipo = utilizador instanceof Cliente ? "CLIENTE" : "ADMIN";

        return new UtilizadorDto(
            utilizador.getId(),
            utilizador.getEmail(),
            utilizador.getNome()
           
        );
    }

    // ========================================
    // ============ DTO -> ENTIDADE ============
    // ========================================


   
    /**
     * Converte DTO -> Utilizador (Cliente ou Admin)
     */
    public static Utilizador mapDtoToUtilizador(UtilizadorDto dto) {
        if (dto == null) return null;

        Utilizador utilizador;

        switch (dto.tipo()) {
            case "CLIENTE":
                utilizador = new Cliente(
                    dto.email(),
                    dto.nome()
                );
                break;

            case "ADMIN":
                utilizador = new Admin(
                    dto.email(),
                    dto.nome()
                    
                );
                break;

            default:
                throw new IllegalArgumentException("Tipo de utilizador inválido: " + dto.tipo());
        }

        // ID não é definido — JPA vai gerar
        return utilizador;
    }
}

