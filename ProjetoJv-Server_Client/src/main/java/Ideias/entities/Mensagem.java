package Ideias.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
public class Mensagem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 4000) private String texto;
    @CreationTimestamp @Column(nullable = false, updatable = false) private LocalDateTime criadoEm;
    @ManyToOne(optional = false) @JoinColumn(nullable = false) private Conversa conversa;
    @ManyToOne(optional = false) @JoinColumn(nullable = false) private Utilizador remetente;

    public Long getId() { return id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public Conversa getConversa() { return conversa; }
    public void setConversa(Conversa conversa) { this.conversa = conversa; }
    public Utilizador getRemetente() { return remetente; }
    public void setRemetente(Utilizador remetente) { this.remetente = remetente; }
}
