package main.Ideias.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.Ideias.entities.Utilizador;
import main.Ideias.services.*;
import main.Ideias.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilizadores")
public class UtilizadorController {

    private final UtilizadorService utilizadorService;

    public UtilizadorController(UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
    }

    // Caso H — Registar novo utilizador (Cliente ou Admin)
    @PostMapping
    public ResponseEntity<Utilizador> criar(@RequestBody Utilizador utilizador) {
        return ResponseEntity.ok(utilizadorService.criarUtilizador(utilizador));
    }

    // Caso I — Listar todos os utilizadores
    @GetMapping
    public List<Utilizador> listar() {
        return utilizadorService.getAll();
    }

    // Caso J — Obter detalhes de um utilizador
    @GetMapping("/{id}")
    public ResponseEntity<Utilizador> detalhes(@PathVariable Long id) {
        return ResponseEntity.ok(utilizadorService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilizador> atualizar(
            @PathVariable Long id,
            @RequestBody UpdateUtilizadorDto dto) {

        return ResponseEntity.ok(
                utilizadorService.atualizarUtilizador(id, dto.nome(), dto.subscricao())
        );
    }


    // Remover utilizador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        utilizadorService.removerUtilizador(id);
        return ResponseEntity.noContent().build();
    }
}
