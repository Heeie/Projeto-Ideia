package pt.ul.fc.css.urbanwheels.mapper;

import java.util.List;
import java.util.stream.Collectors;

import main.Ideias.dto.*;
import main.Ideias.entities.*;
import main.Ideias.enums.*;

public class Mapper {

    // ========================================
    // ============ ENTIDADE -> DTO ============
    // ========================================

    public static EstacaoDto mapToEstacaoDto(Estacao estacao) {
        if (estacao == null) return null;

        List<BicicletaDto> bicicletaDtos = estacao.getBicicletas() == null ?
            List.of() :
            estacao.getBicicletas()
                   .stream()
                   .map(Mapper::mapToBicicletaDto)
                   .collect(Collectors.toList());

        return new EstacaoDto(
            estacao.getId(),
            estacao.getNome(),
            estacao.getDocas(),
            estacao.getLatitude(),
            estacao.getLongitude(),
            bicicletaDtos
        );
    }

    public static BicicletaDto mapToBicicletaDto(Bicicleta bicicleta) {
        if (bicicleta == null) return null;
        Long estacaoId = bicicleta.getEstacao() != null ? bicicleta.getEstacao().getId() : null;

        return new BicicletaDto(
            bicicleta.getId(),
            bicicleta.getModelo(),
            bicicleta.getReservadoPor(),
            bicicleta.isEletrica(),
            bicicleta.getEstado(),
            estacaoId
        );
    }

    public static UtilizadorDto mapToUtilizadorDto(Utilizador utilizador) {
        if (utilizador == null) return null;

        String tipo = utilizador instanceof Cliente ? "CLIENTE" : "ADMIN";

        return new UtilizadorDto(
            utilizador.getId(),
            utilizador.getEmail(),
            utilizador.getNome(),
            utilizador.getSubscricao(),
            tipo
        );
    }

    // ========================================
    // ============ DTO -> ENTIDADE ============
    // ========================================

    public static Estacao mapDtoToEstacao(EstacaoDto dto) {
        if (dto == null) return null;

        Estacao estacao = new Estacao();
        estacao.setNome(dto.nome());
        estacao.setDocas(dto.docas());
        estacao.setLatitude(dto.latitude());
        estacao.setLongitude(dto.longitude());

        if (dto.bicicletas() != null) {
            List<Bicicleta> bicicletas = dto.bicicletas()
                .stream()
                .map(b -> mapDtoToBicicleta(b, estacao))
                .collect(Collectors.toList());

            estacao.setBicicletas(bicicletas);
        }

        return estacao;
    }

    public static Bicicleta mapDtoToBicicleta(BicicletaDto dto, Estacao estacao) {
        if (dto == null) return null;

        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setModelo(dto.modelo());
        bicicleta.setEstado(dto.estado() != null ? dto.estado() : BicicletaStatus.DISPONIVEL);
        bicicleta.setEstacao(estacao);

        return bicicleta;
    }

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
                    dto.nome(),
                    dto.subscricao()
                );
                break;

            case "ADMIN":
                utilizador = new Admin(
                    dto.email(),
                    dto.nome(),
                    dto.subscricao()
                );
                break;

            default:
                throw new IllegalArgumentException("Tipo de utilizador inválido: " + dto.tipo());
        }

        // ID não é definido — JPA vai gerar
        return utilizador;
    }
}

