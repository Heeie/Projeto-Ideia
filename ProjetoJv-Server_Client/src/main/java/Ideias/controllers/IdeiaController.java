package Ideias.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ideias.dto.IdeiaDto;
import Ideias.entities.Ideia;
import Ideias.services.IdeiaService;

@RestController
@RequestMapping("/api/ideias")
public class IdeiaController {

    private final IdeiaService ideiaService;

    public IdeiaController(IdeiaService ideiaService) {
        this.ideiaService = ideiaService;
    }

    // Criar ideia
    @PostMapping
    public ResponseEntity<Ideia> criar(@RequestBody IdeiaDto dto) {
        return ResponseEntity.ok(ideiaService.criarIdeia(dto));
    }

    // Listar todas
    @GetMapping
    public List<Ideia> listar() {
        return ideiaService.getAll();
    }

    // Obter por id
    @GetMapping("/{id}")
    public ResponseEntity<Ideia> detalhes(@PathVariable Long id) {
        return ResponseEntity.ok(ideiaService.getById(id));
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Ideia> atualizar(
            @PathVariable Long id,
            @RequestBody IdeiaDto dto) {

        return ResponseEntity.ok(
                ideiaService.atualizarIdeia(id, dto)
        );
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {

        ideiaService.removerIdeia(id);

        return ResponseEntity.noContent().build();
    }
}