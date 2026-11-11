package Ideias.entities;

import jakarta.persistence.*;

@Entity
public class Preferencias {
    @Id private Long utilizadorId;
    @MapsId @OneToOne(optional = false) @JoinColumn(name = "utilizador_id") private Utilizador utilizador;
    @Column(nullable = false) private boolean novasIdeias = true;
    @Column(nullable = false) private boolean comentarios = true;

    public Long getUtilizadorId() { return utilizadorId; }
    public Utilizador getUtilizador() { return utilizador; }
    public void setUtilizador(Utilizador utilizador) { this.utilizador = utilizador; }
    public boolean isNovasIdeias() { return novasIdeias; }
    public void setNovasIdeias(boolean novasIdeias) { this.novasIdeias = novasIdeias; }
    public boolean isComentarios() { return comentarios; }
    public void setComentarios(boolean comentarios) { this.comentarios = comentarios; }
}
