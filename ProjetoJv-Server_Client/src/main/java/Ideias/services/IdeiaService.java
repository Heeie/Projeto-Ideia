package Ideias.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Ideias.dto.IdeiaDto;
import Ideias.entities.Cliente;
import Ideias.entities.Ideia;
import Ideias.exceptions.ResourceNotFoundException;
import Ideias.repositories.ClienteRepository;
import Ideias.repositories.ComentarioRepository;
import Ideias.repositories.IdeiaRepository;

@Service
@Transactional
public class IdeiaService {
    private final IdeiaRepository ideiaRepository;
    private final ClienteRepository clienteRepository;
    private final ComentarioRepository comentarioRepository;

    public IdeiaService(IdeiaRepository ideiaRepository, ClienteRepository clienteRepository, ComentarioRepository comentarioRepository) {
        this.ideiaRepository = ideiaRepository;
        this.clienteRepository = clienteRepository;
        this.comentarioRepository = comentarioRepository;
    }

    public Ideia criarIdeia(IdeiaDto dto) {
        Ideia ideia = new Ideia();
        aplicarDto(ideia, dto);
        return ideiaRepository.save(ideia);
    }

    @Transactional(readOnly = true)
    public List<Ideia> getAll() { return ideiaRepository.findAll(); }

    @Transactional(readOnly = true)
    public Ideia getById(Long id) {
        return ideiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ideia não encontrada."));
    }

    public Ideia atualizarIdeia(Long id, IdeiaDto dto) {
        Ideia ideia = getById(id);
        aplicarDto(ideia, dto);
        return ideiaRepository.save(ideia);
    }

    public void removerIdeia(Long id) {
        Ideia ideia = getById(id);
        ideia.getFavoritaPor().forEach(utilizador -> utilizador.getIdeiasFav().remove(ideia));
        ideia.getGostadaPor().forEach(utilizador -> utilizador.getIdeiasGostadas().remove(ideia));
        comentarioRepository.findByIdeia_Id(id).forEach(comentario -> {
            comentario.getGostadaPor().clear();
            comentarioRepository.delete(comentario);
        });
        ideiaRepository.delete(ideia);
    }

    private void aplicarDto(Ideia ideia, IdeiaDto dto) {
        ideia.setNome(dto.nome().trim());
        ideia.setDescricao(dto.descricao() == null ? null : dto.descricao().trim());
        ideia.setImagem(dto.imagem());
        ideia.setCategoria(dto.categoria());
        ideia.setEstado(dto.estado());
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        ideia.setCliente(cliente);
    }
}
