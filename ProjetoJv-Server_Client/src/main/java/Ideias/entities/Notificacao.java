package Ideias.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
public class Notificacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(nullable = false) private Utilizador destinatario;
    @Column(nullable = false, length = 30) private String tipo;
    @Column(nullable = false, length = 300) private String texto;
    @Column(length = 300) private String link;
    @Column(nullable = false) private boolean lida;
    @CreationTimestamp @Column(nullable = false, updatable = false) private LocalDateTime criadoEm;

    public Long getId() { return id; }
    public Utilizador getDestinatario() { return destinatario; }
    public void setDestinatario(Utilizador destinatario) { this.destinatario = destinatario; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public boolean isLida() { return lida; }
    public void setLida(boolean lida) { this.lida = lida; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
