package Ideias.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByIdeia_IdOrderByCriadoEmAsc(Long ideiaId);
    List<Comentario> findByIdeia_Id(Long ideiaId);
}
