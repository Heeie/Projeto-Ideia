package Ideias.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ideias.dto.IdeiaDto;
import Ideias.dto.IdeiaResponseDto;
import Ideias.entities.Ideia;
import Ideias.services.IdeiaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ideias")
public class IdeiaController {
    private final IdeiaService ideiaService;

    public IdeiaController(IdeiaService ideiaService) { this.ideiaService = ideiaService; }

    @PostMapping
    public ResponseEntity<IdeiaResponseDto> criar(@Valid @RequestBody IdeiaDto dto) {
        return ResponseEntity.ok(resposta(ideiaService.criarIdeia(dto)));
    }

    @GetMapping
    public List<IdeiaResponseDto> listar() {
        return ideiaService.getAll().stream().map(IdeiaController::resposta).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdeiaResponseDto> detalhes(@PathVariable Long id) {
        return ResponseEntity.ok(resposta(ideiaService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdeiaResponseDto> atualizar(@PathVariable Long id,
            @Valid @RequestBody IdeiaDto dto) {
        return ResponseEntity.ok(resposta(ideiaService.atualizarIdeia(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        ideiaService.removerIdeia(id);
        return ResponseEntity.noContent().build();
    }

    public static IdeiaResponseDto resposta(Ideia ideia) {
        return new IdeiaResponseDto(ideia.getId(), ideia.getNome(), ideia.getDescricao(), ideia.getImagem(),
                ideia.getCategoria(), ideia.getEstado(),
                ideia.getCliente() == null ? null : ideia.getCliente().getId(),
                ideia.getCliente() == null ? null : ideia.getCliente().getNome());
    }
}
