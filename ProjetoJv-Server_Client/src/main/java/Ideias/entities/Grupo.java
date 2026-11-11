package Ideias.entities;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
public class Grupo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 80) private String nome;
    @Column(nullable = false, length = 500) private String descricao;
    @ManyToOne(optional = false) @JoinColumn(nullable = false) private Cliente criador;
    @ManyToMany @JoinTable(name = "grupo_membros", joinColumns = @JoinColumn(name = "grupo_id"), inverseJoinColumns = @JoinColumn(name = "utilizador_id"))
    private Set<Utilizador> membros = new HashSet<>();

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Cliente getCriador() { return criador; }
    public void setCriador(Cliente criador) { this.criador = criador; }
    public Set<Utilizador> getMembros() { return membros; }
}
