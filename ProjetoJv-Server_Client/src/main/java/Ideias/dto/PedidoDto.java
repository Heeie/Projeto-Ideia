package Ideias.dto;

import Ideias.enums.EstadoPedido;
import Ideias.enums.TypePedido;


public record PedidoDto(

    Long id,
    TypePedido tipo,
    EstadoPedido estado,
    Long pedinte,
    Long julgador

) {}

  
