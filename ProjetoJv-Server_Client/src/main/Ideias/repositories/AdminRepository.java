package main.Ideias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import main.Ideias.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
