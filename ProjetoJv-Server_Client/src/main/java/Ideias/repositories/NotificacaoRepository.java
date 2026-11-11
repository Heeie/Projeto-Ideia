package Ideias.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Notificacao;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByDestinatario_IdOrderByCriadoEmDesc(Long utilizadorId);
}
