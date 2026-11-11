package Ideias.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Amizade;

public interface AmizadeRepository extends JpaRepository<Amizade, Long> {
    List<Amizade> findByRequerente_IdOrDestinatario_Id(Long requerenteId, Long destinatarioId);
    Optional<Amizade> findByRequerente_IdAndDestinatario_IdOrRequerente_IdAndDestinatario_Id(
            Long a1, Long a2, Long b1, Long b2);
}
