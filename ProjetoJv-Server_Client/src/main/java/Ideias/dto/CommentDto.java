package Ideias.dto;


import Ideias.entities.Ideia;
import Ideias.entities.Utilizador;
import Ideias.enums.EstadoComment;

public record CommentDto(
    Long id,
    String comment_text,
    EstadoComment estado,
    int likes,
    Ideia ideia,
    Utilizador commentor
) {}

    

    
    

