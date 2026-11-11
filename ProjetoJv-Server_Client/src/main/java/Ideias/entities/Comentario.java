package Ideias.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
public class Comentario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 2000) private String texto;
    @CreationTimestamp @Column(nullable = false, updatable = false) private LocalDateTime criadoEm;
    @ManyToOne(optional = false) @JoinColumn(nullable = false) private Ideia ideia;
    @ManyToOne(optional = false) @JoinColumn(nullable = false) private Utilizador autor;
    @ManyToMany @JoinTable(name = "comentario_gostos", joinColumns = @JoinColumn(name = "comentario_id"), inverseJoinColumns = @JoinColumn(name = "utilizador_id"))
    private Set<Utilizador> gostadaPor = new HashSet<>();

    public Long getId() { return id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public Ideia getIdeia() { return ideia; }
    public void setIdeia(Ideia ideia) { this.ideia = ideia; }
    public Utilizador getAutor() { return autor; }
    public void setAutor(Utilizador autor) { this.autor = autor; }
    public Set<Utilizador> getGostadaPor() { return gostadaPor; }
}
