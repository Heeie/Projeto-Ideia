package Ideias.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ideias.entities.Utilizador;
import Ideias.repositories.UtilizadorRepository;

@Service
public class UtilizadorService {

    private final UtilizadorRepository utilizadorRepository;

    @Autowired
    public UtilizadorService(UtilizadorRepository utilizadorRepository) {
        this.utilizadorRepository = utilizadorRepository;
    }

    // ===============================
    // Listar todos
    // ===============================
    public List<Utilizador> getAll() {
        return utilizadorRepository.findAll();
    }

    // ===============================
    // Procurar por ID
    // ===============================
    public Utilizador getById(Long id) {
        return utilizadorRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Utilizador não encontrado."));
    }

    // ===============================
    // Procurar por email
    // ===============================
    public Utilizador getByEmail(String email) {
        return utilizadorRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Utilizador não encontrado."));
    }

    // ===============================
    // Criar
    // ===============================
    public <T extends Utilizador> T criarUtilizador(T utilizador) {

        validarUtilizador(utilizador);

        if (utilizadorRepository.findByEmail(utilizador.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe um utilizador com esse email.");
        }

        return utilizadorRepository.save(utilizador);
    }

    // ===============================
    // Atualizar
    // ===============================
    public Utilizador atualizarUtilizador(Long id,
                                          String nome,
                                          String email,
                                          String password) {

        Utilizador utilizador = getById(id);

        if (nome != null && !nome.isBlank())
            utilizador.setNome(nome);

        if (email != null && !email.isBlank())
            utilizador.setEmail(email);

        if (password != null && !password.isBlank())
            utilizador.setPassword(password);

        validarUtilizador(utilizador);

        return utilizadorRepository.save(utilizador);
    }

    // ===============================
    // Remover
    // ===============================
    public void removerUtilizador(Long id) {

        if (!utilizadorRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilizador não existe.");
        }

        utilizadorRepository.deleteById(id);
    }

    // ===============================
    // Validação
    // ===============================
    private void validarUtilizador(Utilizador utilizador) {

        if (utilizador.getNome() == null || utilizador.getNome().isBlank())
            throw new IllegalArgumentException("Nome obrigatório.");

        if (utilizador.getEmail() == null || utilizador.getEmail().isBlank())
            throw new IllegalArgumentException("Email obrigatório.");

        if (!utilizador.getEmail().contains("@"))
            throw new IllegalArgumentException("Email inválido.");

        if (utilizador.getPassword() == null || utilizador.getPassword().isBlank())
            throw new IllegalArgumentException("Password obrigatória.");
    }

}