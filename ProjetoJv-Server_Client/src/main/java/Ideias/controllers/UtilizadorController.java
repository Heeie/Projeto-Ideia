package Ideias.controllers;

import java.util.List;
import java.util.Locale;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Ideias.dto.UtilizadorDto;
import Ideias.dto.AtualizarPerfilDto;
import Ideias.dto.UtilizadorResponseDto;
import Ideias.entities.Cliente;
import Ideias.entities.Utilizador;
import Ideias.services.UtilizadorService;
import jakarta.validation.Valid;
import Ideias.security.ActorGuard;
import Ideias.dto.UtilizadorResumoDto;
import jakarta.servlet.http.HttpServletRequest;
import Ideias.security.ApiTokenService;

@RestController
@RequestMapping("/api/utilizadores")
public class UtilizadorController {
    private final UtilizadorService utilizadorService;
    private final ApiTokenService tokens;

    public UtilizadorController(UtilizadorService utilizadorService, ApiTokenService tokens) {
        this.utilizadorService = utilizadorService;
        this.tokens = tokens;
    }

    @PostMapping
    public ResponseEntity<UtilizadorResponseDto> criar(@Valid @RequestBody UtilizadorDto dto) {
        throw new IllegalArgumentException("Utiliza o endpoint /api/auth/registo para criar uma conta.");
    }

    @GetMapping
    public List<UtilizadorResumoDto> listar() {
        return utilizadorService.getAll().stream().map(u -> new UtilizadorResumoDto(u.getId(), u.getNome(), u instanceof Cliente ? "CLIENTE" : "ADMIN")).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilizadorResponseDto> detalhes(@PathVariable Long id, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, id);
        return ResponseEntity.ok(resposta(utilizadorService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilizadorResponseDto> atualizar(@PathVariable Long id,
            @Valid @RequestBody UtilizadorDto dto, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, id);
        Utilizador atualizado = utilizadorService.atualizarUtilizador(id,
                dto.nome(), dto.email(), dto.password());
        return ResponseEntity.ok(resposta(atualizado));
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<UtilizadorResponseDto> atualizarPerfil(@PathVariable Long id,
            @Valid @RequestBody AtualizarPerfilDto dto, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, id);
        Utilizador atualizado = utilizadorService.atualizarUtilizador(id, dto.nome(), dto.email(), null);
        return ResponseEntity.ok(resposta(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id, HttpServletRequest request) {
        ActorGuard.requireCurrentUser(request, id);
        utilizadorService.removerUtilizador(id);
        tokens.revokeUser(id);
        return ResponseEntity.noContent().build();
    }

    private static UtilizadorResponseDto resposta(Utilizador utilizador) {
        String tipo = utilizador instanceof Cliente ? "CLIENTE" : "ADMIN";
        return new UtilizadorResponseDto(utilizador.getId(), utilizador.getEmail(), utilizador.getNome(), tipo);
    }
}
