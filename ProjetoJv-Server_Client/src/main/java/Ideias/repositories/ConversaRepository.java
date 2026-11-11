package Ideias.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Conversa;

public interface ConversaRepository extends JpaRepository<Conversa, Long> {
    List<Conversa> findDistinctByParticipantes_Id(Long utilizadorId);
}
