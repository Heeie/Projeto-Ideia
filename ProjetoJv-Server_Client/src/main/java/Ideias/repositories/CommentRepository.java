package Ideias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Ideias.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    
}
