package Ideias.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ideias.dto.UtilizadorDto;
import Ideias.entities.Utilizador;
import Ideias.services.UtilizadorService;

@RestController
@RequestMapping("/api/utilizadores")
public class UtilizadorController {

    private final UtilizadorService utilizadorService;

    public UtilizadorController(UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
    }

    // Criar
    @PostMapping
    public ResponseEntity<Utilizador> criar(@RequestBody Utilizador utilizador) {

        return ResponseEntity.ok(
                utilizadorService.criarUtilizador(utilizador)
        );
    }

    // Listar
    @GetMapping
    public List<Utilizador> listar() {
        return utilizadorService.getAll();
    }

    // Procurar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Utilizador> detalhes(@PathVariable Long id) {

        return ResponseEntity.ok(
                utilizadorService.getById(id)
        );
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Utilizador> atualizar(
            @PathVariable Long id,
            @RequestBody UtilizadorDto dto) {

        return ResponseEntity.ok(
                utilizadorService.atualizarUtilizador(
                        id,
                        dto.nome(),
                        dto.email(),
                        dto.password()
                )
        );
    }

    // Remover
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {

        utilizadorService.removerUtilizador(id);

        return ResponseEntity.noContent().build();
    }

}