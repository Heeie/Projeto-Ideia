package Ideias.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findDistinctByMembros_Id(Long utilizadorId);
}
