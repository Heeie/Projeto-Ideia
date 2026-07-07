package Ideias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	
}
