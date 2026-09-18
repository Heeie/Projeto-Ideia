package Ideias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import Ideias.entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
}


