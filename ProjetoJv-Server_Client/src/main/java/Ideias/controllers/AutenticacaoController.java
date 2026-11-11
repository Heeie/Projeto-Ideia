package Ideias.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ideias.dto.LoginDto;
import Ideias.dto.RegistoDto;
import Ideias.dto.UtilizadorResponseDto;
import Ideias.entities.Cliente;
import Ideias.entities.Utilizador;
import Ideias.services.UtilizadorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {
    private final UtilizadorService utilizadorService;

    public AutenticacaoController(UtilizadorService utilizadorService) {
        this.utilizadorService = utilizadorService;
    }

    @PostMapping("/registo")
    public ResponseEntity<UtilizadorResponseDto> registar(@Valid @RequestBody RegistoDto dto) {
        Cliente cliente = utilizadorService.criarUtilizador(
                new Cliente(dto.email(), dto.nome(), dto.password()));
        return ResponseEntity.ok(resposta(cliente));
    }

    @PostMapping("/login")
    public ResponseEntity<UtilizadorResponseDto> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(resposta(utilizadorService.autenticar(dto.email(), dto.password())));
    }

    private static UtilizadorResponseDto resposta(Utilizador user) {
        return new UtilizadorResponseDto(user.getId(), user.getEmail(), user.getNome(),
                user instanceof Cliente ? "CLIENTE" : "ADMIN");
    }
}
