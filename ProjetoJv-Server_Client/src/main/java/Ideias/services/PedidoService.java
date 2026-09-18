package Ideias.services;

import java.util.List;

import org.springframework.stereotype.Service;

import Ideias.dto.PedidoDto;
import Ideias.entities.Pedido;
import Ideias.repositories.PedidoRepository;

@Service
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // Criar Pedido
    public Pedido criarPedido(PedidoDto dto) {
        Pedido pedido = new Pedido();

        pedido.setEstado(dto.estado());
        pedido.setType(dto.tipo());
        pedido.setPedinte(dto.pedinte());
        pedido.setJulgador(dto.julgador());

        return pedidoRepository.save(pedido);
    }

     // Listar todas
    public List<Pedido> getAll() {
        return pedidoRepository.findAll();
    }

    // Procurar por id
    public Pedido getById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Pedido não encontrado."));
    }

    // Atualizar
    public Pedido atualizarEstadoPedido(Long id, PedidoDto dto) {

        Pedido pedido = getById(id);

        pedido.setEstado(dto.estado());

        return pedidoRepository.save(pedido);
    }

    // Remover
    public void removerIdeia(Long id) {
        Pedido pedido = getById(id);
        pedidoRepository.delete(pedido);
    }
}
