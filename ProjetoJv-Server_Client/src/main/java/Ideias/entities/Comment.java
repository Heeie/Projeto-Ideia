package Ideias.entities;


import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import Ideias.enums.EstadoComment;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment_text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoComment estado;
    

    @Column(nullable = true)
    private int likes;

    @ManyToOne
    @JoinColumn(name = "ideia_id")
    @JsonBackReference
    private Ideia ideia;

    @OneToOne
    @JoinColumn(name = "commentor_id")
    private Utilizador commentor;

    public Comment() {}

    public Comment(String texto, int likes, Ideia ideia, Utilizador commentor) {
        this.comment_text = texto;
        this.likes = likes;
        this.ideia = ideia;
        this.commentor = commentor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return  this.comment_text;
    }

    public void setText(String texto) {
        this.comment_text = texto;
    }

    public int getLikes() {
        return this.likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    public Ideia getIdeia() {
        return this.ideia;
    }

    public void setIdeia(Ideia d) {
        this.ideia = d;
    }

    public Utilizador  getCommentor() {
        return this.commentor;
    }

    public void setCommentor(Utilizador u) {
        this.commentor = u;
    }

    // EstadoComment estado;

     public EstadoComment getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoComment estado) {
        this.estado = estado;
    }
}


