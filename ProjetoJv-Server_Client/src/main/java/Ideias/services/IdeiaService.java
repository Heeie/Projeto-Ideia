package Ideias.services;

import java.util.List;

import org.springframework.stereotype.Service;

import Ideias.dto.IdeiaDto;
import Ideias.entities.Cliente;
import Ideias.entities.Ideia;
import Ideias.repositories.IdeiaRepository;

@Service
public class IdeiaService {

    private final IdeiaRepository ideiaRepository;

    public IdeiaService(IdeiaRepository ideiaRepository) {
        this.ideiaRepository = ideiaRepository;
    }

    // Criar ideia
    public Ideia criarIdeia(IdeiaDto dto) {

        Ideia ideia = new Ideia();

        ideia.setNome(dto.nome());
        ideia.setDescricao(dto.descricao());
        ideia.setCategoria(dto.categoria());
        ideia.setEstado(dto.estado());

        // Caso já tenhas ClienteRepository,
        // aqui deverás procurar o cliente pelo id.
        // Por agora fica vazio.
        ideia.setCliente(null);

        return ideiaRepository.save(ideia);
    }

    // Listar todas
    public List<Ideia> getAll() {
        return ideiaRepository.findAll();
    }

    // Procurar por id
    public Ideia getById(Long id) {
        return ideiaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ideia não encontrada."));
    }

    // Atualizar
    public Ideia atualizarIdeia(Long id, IdeiaDto dto) {

        Ideia ideia = getById(id);

        ideia.setNome(dto.nome());
        ideia.setDescricao(dto.descricao());
        ideia.setCategoria(dto.categoria());
        ideia.setEstado(dto.estado());

        return ideiaRepository.save(ideia);
    }

    // Remover
    public void removerIdeia(Long id) {

        Ideia ideia = getById(id);

        ideiaRepository.delete(ideia);
    }

}