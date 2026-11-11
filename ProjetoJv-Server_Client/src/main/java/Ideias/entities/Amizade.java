package Ideias.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"requerente_id", "destinatario_id"}))
public class Amizade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "requerente_id", nullable = false) private Utilizador requerente;
    @ManyToOne(optional = false) @JoinColumn(name = "destinatario_id", nullable = false) private Utilizador destinatario;
    @Column(nullable = false, length = 20) private String estado = "PENDENTE";
    @CreationTimestamp @Column(nullable = false, updatable = false) private LocalDateTime criadoEm;

    public Long getId() { return id; }
    public Utilizador getRequerente() { return requerente; }
    public void setRequerente(Utilizador requerente) { this.requerente = requerente; }
    public Utilizador getDestinatario() { return destinatario; }
    public void setDestinatario(Utilizador destinatario) { this.destinatario = destinatario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
