package Ideias.services;


import java.util.List;

import org.springframework.stereotype.Service;

import Ideias.dto.CommentDto;
import Ideias.entities.Comment;
import Ideias.repositories.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository cRepository) {
        this.commentRepository = cRepository;
    }
   
    public Comment criarComment(CommentDto dto) {
        Comment c = new Comment();

        c.setText(dto.comment_text());
        c.setEstado(dto.estado());
        c.setCommentor(dto.commentor());
        c.setLikes(dto.likes());
        c.setIdeia(dto.ideia());
        c.setCommentor(dto.commentor());

        return commentRepository.save(c);
    }

    // Listar todas
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    // Procurar por id
    public Comment getById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Comentário não foi encontrado."));
    }

    // Atualizar
    public Comment atualizarComment(Long id, CommentDto dto) {

        Comment c = getById(id);

        c.setText(dto.comment_text());
        c.setEstado(dto.estado());
        c.setCommentor(dto.commentor());
        c.setLikes(dto.likes());
        c.setIdeia(dto.ideia());
        c.setCommentor(dto.commentor());

        return commentRepository.save(c);
    }

    // Remover
    public void removerComment(Long id) {
        Comment c = getById(id);
        commentRepository.delete(c);
    }
}
