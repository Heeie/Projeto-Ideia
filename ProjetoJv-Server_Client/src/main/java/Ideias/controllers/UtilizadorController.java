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

@RestController
@RequestMapping("/api/utilizadores")
public class UtilizadorController {
    private final UtilizadorService utilizadorService;

    public UtilizadorController(UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
    }

    @PostMapping
    public ResponseEntity<UtilizadorResponseDto> criar(@Valid @RequestBody UtilizadorDto dto) {
        if (!"CLIENTE".equals(dto.tipo().trim().toUpperCase(Locale.ROOT)))
            throw new IllegalArgumentException("Só é permitido registar contas de cliente.");
        Cliente novo = new Cliente(dto.email(), dto.nome(), dto.password());
        return ResponseEntity.ok(resposta(utilizadorService.criarUtilizador(novo)));
    }

    @GetMapping
    public List<UtilizadorResponseDto> listar() {
        return utilizadorService.getAll().stream().map(UtilizadorController::resposta).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilizadorResponseDto> detalhes(@PathVariable Long id) {
        return ResponseEntity.ok(resposta(utilizadorService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilizadorResponseDto> atualizar(@PathVariable Long id,
            @Valid @RequestBody UtilizadorDto dto) {
        Utilizador atualizado = utilizadorService.atualizarUtilizador(id,
                dto.nome(), dto.email(), dto.password());
        return ResponseEntity.ok(resposta(atualizado));
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<UtilizadorResponseDto> atualizarPerfil(@PathVariable Long id,
            @Valid @RequestBody AtualizarPerfilDto dto) {
        Utilizador atualizado = utilizadorService.atualizarUtilizador(id, dto.nome(), dto.email(), null);
        return ResponseEntity.ok(resposta(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        utilizadorService.removerUtilizador(id);
        return ResponseEntity.noContent().build();
    }

    private static UtilizadorResponseDto resposta(Utilizador utilizador) {
        String tipo = utilizador instanceof Cliente ? "CLIENTE" : "ADMIN";
        return new UtilizadorResponseDto(utilizador.getId(), utilizador.getEmail(), utilizador.getNome(), tipo);
    }
}
