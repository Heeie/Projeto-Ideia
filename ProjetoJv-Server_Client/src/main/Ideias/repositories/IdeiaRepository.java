package main.Ideias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.Ideias.entities.Ideia;

public interface IdeiaRepository extends JpaRepository<Ideia, Long> {
}
