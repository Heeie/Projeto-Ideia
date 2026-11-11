package Ideias.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ideias.dto.IdeiaDto;
import Ideias.dto.IdeiaResponseDto;
import Ideias.entities.Ideia;
import Ideias.services.IdeiaService;
import Ideias.security.ActorGuard;
import Ideias.enums.IdeiaStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ideias")
public class IdeiaController {
    private final IdeiaService ideiaService;

    public IdeiaController(IdeiaService ideiaService) { this.ideiaService = ideiaService; }

    @PostMapping
    public ResponseEntity<IdeiaResponseDto> criar(@Valid @RequestBody IdeiaDto dto, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, dto.clienteId());
        return ResponseEntity.ok(resposta(ideiaService.criarIdeia(dto)));
    }

    @GetMapping
    public List<IdeiaResponseDto> listar() {
        return ideiaService.getAll().stream().filter(i -> i.getEstado() == IdeiaStatus.PUBLICA).map(IdeiaController::resposta).toList();
    }

    @GetMapping("/minhas")
    public List<IdeiaResponseDto> minhas(HttpServletRequest request) {
        Long userId = ActorGuard.currentUserId(request);
        return ideiaService.getAll().stream().filter(i -> i.getCliente().getId().equals(userId)).map(IdeiaController::resposta).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdeiaResponseDto> detalhes(@PathVariable Long id, HttpServletRequest request) {
        var ideia = ideiaService.getById(id);
        if (ideia.getEstado() != IdeiaStatus.PUBLICA) ActorGuard.requireCurrentUser(request, ideia.getCliente().getId());
        return ResponseEntity.ok(resposta(ideia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdeiaResponseDto> atualizar(@PathVariable Long id,
            @Valid @RequestBody IdeiaDto dto, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, dto.clienteId());
        ActorGuard.requireCurrentUser(request, ideiaService.getById(id).getCliente().getId());
        return ResponseEntity.ok(resposta(ideiaService.atualizarIdeia(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, ideiaService.getById(id).getCliente().getId());
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
