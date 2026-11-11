package Ideias.services;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Ideias.entities.Ideia;
import Ideias.entities.Cliente;
import Ideias.entities.Utilizador;
import Ideias.exceptions.ConflictException;
import Ideias.exceptions.ResourceNotFoundException;
import Ideias.exceptions.UnauthorizedException;
import Ideias.repositories.IdeiaRepository;
import Ideias.repositories.UtilizadorRepository;

@Service
@Transactional
public class UtilizadorService {
    private final UtilizadorRepository utilizadorRepository;
    private final IdeiaRepository ideiaRepository;
    private final PasswordService passwordService;

    public UtilizadorService(UtilizadorRepository utilizadorRepository, IdeiaRepository ideiaRepository,
            PasswordService passwordService) {
        this.utilizadorRepository = utilizadorRepository;
        this.ideiaRepository = ideiaRepository;
        this.passwordService = passwordService;
    }

    @Transactional(readOnly = true)
    public List<Utilizador> getAll() { return utilizadorRepository.findAll(); }

    @Transactional(readOnly = true)
    public Utilizador getById(Long id) {
        return utilizadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado."));
    }

    @Transactional(readOnly = true)
    public Utilizador getByEmail(String email) {
        return utilizadorRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("Utilizador não encontrado."));
    }

    public <T extends Utilizador> T criarUtilizador(T utilizador) {
        validarUtilizador(utilizador);
        if (utilizadorRepository.findByEmail(utilizador.getEmail()).isPresent())
            throw new ConflictException("Já existe um utilizador com esse email.");
        utilizador.setPassword(passwordService.encode(utilizador.getPassword()));
        return utilizadorRepository.save(utilizador);
    }

    public Utilizador autenticar(String email, String password) {
        Utilizador utilizador = utilizadorRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new UnauthorizedException("Email ou palavra-passe incorretos."));
        if (!passwordService.matches(password, utilizador.getPassword()))
            throw new UnauthorizedException("Email ou palavra-passe incorretos.");
        if (passwordService.needsUpgrade(utilizador.getPassword())) {
            utilizador.setPassword(passwordService.encode(password));
            utilizadorRepository.save(utilizador);
        }
        return utilizador;
    }

    public Utilizador atualizarUtilizador(Long id, String nome, String email, String password) {
        Utilizador utilizador = getById(id);
        if (nome != null && !nome.isBlank()) utilizador.setNome(nome.trim());
        if (email != null && !email.isBlank()) {
            String normalizedEmail = normalizeEmail(email);
            utilizadorRepository.findByEmail(normalizedEmail)
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> { throw new ConflictException("Já existe um utilizador com esse email."); });
            utilizador.setEmail(normalizedEmail);
        }
        if (password != null && !password.isBlank()) utilizador.setPassword(passwordService.encode(password));
        validarUtilizador(utilizador);
        return utilizadorRepository.save(utilizador);
    }

    public void removerUtilizador(Long id) {
        Utilizador utilizador = getById(id);
        utilizador.getIdeiasFav().clear();
        utilizador.getIdeiasGostadas().clear();
        utilizadorRepository.delete(utilizador);
    }

    private void validarUtilizador(Utilizador utilizador) {
        if (utilizador.getNome() == null || utilizador.getNome().isBlank())
            throw new IllegalArgumentException("Nome obrigatório.");
        utilizador.setNome(utilizador.getNome().trim());
        if (utilizador.getEmail() == null || utilizador.getEmail().isBlank())
            throw new IllegalArgumentException("Email obrigatório.");
        utilizador.setEmail(normalizeEmail(utilizador.getEmail()));
        if (!utilizador.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new IllegalArgumentException("Email inválido.");
        if (utilizador.getPassword() == null || utilizador.getPassword().isBlank())
            throw new IllegalArgumentException("Password obrigatória.");
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
