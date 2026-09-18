package Ideias.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Ideias.dto.PedidoDto;
import Ideias.entities.Pedido;
import Ideias.services.PedidoService;


@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Criar pedido
    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody PedidoDto dto) {
        return ResponseEntity.ok(pedidoService.criarPedido(dto));
    }

    // Listar todos os pedidos
    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.getAll();
    }

    // Obter por id
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> detalhes(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getById(id));
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarEstado(
            @PathVariable Long id,
            @RequestBody PedidoDto dto) {

        return ResponseEntity.ok(
                pedidoService.atualizarEstadoPedido(id, dto)
        );
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {

        pedidoService.removerIdeia(id);

        return ResponseEntity.noContent().build();
    }

}
