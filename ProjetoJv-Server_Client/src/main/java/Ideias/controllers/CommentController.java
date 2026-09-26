package Ideias.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Ideias.dto.CommentDto;
import Ideias.entities.Comment;
import Ideias.services.CommentService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> criar(@RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.criarComment(dto));
    }

    // Listar todas
    @GetMapping
    public List<Comment> listar() {
        return commentService.getAll();
    }

    // Obter por id
    @GetMapping("/{id}")
    public ResponseEntity<Comment> detalhes(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Comment> atualizar(
            @PathVariable Long id,
            @RequestBody CommentDto dto) {

        return ResponseEntity.ok(
                commentService.atualizarComment(id, dto)
        );
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        commentService.removerComment(id);
        return ResponseEntity.noContent().build();
    }
}