package Ideias.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;

import Ideias.dto.LoginDto;
import Ideias.dto.RegistoDto;
import Ideias.dto.SessaoResponseDto;
import Ideias.entities.Cliente;
import Ideias.entities.Utilizador;
import Ideias.services.UtilizadorService;
import Ideias.security.ApiTokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {
    private final UtilizadorService utilizadorService;
    private final ApiTokenService tokens;

    public AutenticacaoController(UtilizadorService utilizadorService, ApiTokenService tokens) {
        this.utilizadorService = utilizadorService;
        this.tokens = tokens;
    }

    @PostMapping("/registo")
    public ResponseEntity<SessaoResponseDto> registar(@Valid @RequestBody RegistoDto dto) {
        Cliente cliente = utilizadorService.criarUtilizador(
                new Cliente(dto.email(), dto.nome(), dto.password()));
        return ResponseEntity.ok(resposta(cliente));
    }

    @PostMapping("/login")
    public ResponseEntity<SessaoResponseDto> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(resposta(utilizadorService.autenticar(dto.email(), dto.password())));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(name = "Authorization", required = false) String header) {
        if (header != null && header.regionMatches(true, 0, "Bearer ", 0, 7)) tokens.revoke(header.substring(7).trim());
        return ResponseEntity.noContent().build();
    }

    private SessaoResponseDto resposta(Utilizador user) {
        String token = tokens.issue(user.getId());
        Instant expiresAt = Instant.now().plus(ApiTokenService.TOKEN_LIFETIME);
        return new SessaoResponseDto(user.getId(), user.getEmail(), user.getNome(),
                user instanceof Cliente ? "CLIENTE" : "ADMIN", token, "Bearer", expiresAt);
    }
}
