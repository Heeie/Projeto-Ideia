package Ideias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Ideia;

public interface IdeiaRepository extends JpaRepository<Ideia, Long> {
}
