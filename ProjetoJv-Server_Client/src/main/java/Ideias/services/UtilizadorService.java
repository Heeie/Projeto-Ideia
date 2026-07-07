package Ideias.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ideias.entities.Utilizador;
import Ideias.enums.*;
import Ideias.repositories.UtilizadorRepository;

@Service
public class UtilizadorService {

    private final UtilizadorRepository utilizadorRepository;

    @Autowired
    public UtilizadorService(UtilizadorRepository utilizadorRepository) {
        this.utilizadorRepository = utilizadorRepository;
    }


    // ===============================
    // Obter todos os utilizadores
    // ===============================
    public List<Utilizador> getAll() {
        return utilizadorRepository.findAll();
    }

    // ===============================
    // Obter utilizador por ID
    // ===============================
    public Utilizador getById(Long id) {
        return utilizadorRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Utilizador com id " + id + " não encontrado."));
    }

    // ===============================
    // Obter utilizador por email
    // ===============================
    public Utilizador getByEmail(String email) {
        return utilizadorRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Utilizador com email '" + email + "' não encontrado."));
    }

    // ===============================
    // Criar utilizador (subclasse)
    // ===============================
    public <T extends Utilizador> T criarUtilizador(T utilizador) {

        validarUtilizador(utilizador);

        if (utilizadorRepository.findByEmail(utilizador.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe um utilizador com o email: " + utilizador.getEmail());
        }

        return utilizadorRepository.save(utilizador);
    }

    // ===============================
    // Atualizar dados de utilizador
    // ===============================
    public Utilizador atualizarUtilizador(Long id, String nome, SubsUtilizador subscricao) {
        Utilizador utilizador = getById(id);

        if (nome != null && !nome.isBlank()) {
            utilizador.setNome(nome);
        }

        if (subscricao != null) {
            utilizador.setSubscricao(subscricao);
        }

        return utilizadorRepository.save(utilizador);
    }

    // ===============================
    // Remover utilizador
    // ===============================
    public void removerUtilizador(Long id) {
        utilizadorRepository.deleteById(id);
    }

    // ===============================
    // Validações básicas
    // ===============================
    private void validarUtilizador(Utilizador utilizador) {

        if (utilizador.getEmail() == null || utilizador.getEmail().isBlank()) {
            throw new IllegalArgumentException("O email do utilizador não pode estar vazio.");
        }

        if (!utilizador.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email inválido.");
        }

        if (utilizador.getNome() == null || utilizador.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do utilizador não pode estar vazio.");
        }

        
    }
}
