package Ideias.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByConversa_IdOrderByCriadoEmAsc(Long conversaId);
    void deleteByConversa_Id(Long conversaId);
}
