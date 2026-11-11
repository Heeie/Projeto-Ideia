package Ideias.entities;

import Ideias.enums.CategoriaIdeia;
import Ideias.enums.IdeiaStatus;
import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Ideia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaIdeia categoria;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdeiaStatus estado;

    @Column(length = 5000)
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String imagem;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToMany(mappedBy = "ideiasFav")
    private Set<Utilizador> favoritaPor = new HashSet<>();

    @ManyToMany(mappedBy = "ideiasGostadas")
    private Set<Utilizador> gostadaPor = new HashSet<>();
    
    public Ideia() {}

    public Ideia(String nome, CategoriaIdeia categoria, Cliente cliente) {
        this.nome = nome;
        this.categoria = categoria;
        this.cliente = cliente;
        this.estado = IdeiaStatus.PRIVADA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return  this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaIdeia getCategoria() {
        return this.categoria;
    }

    public void setCategoria(CategoriaIdeia categoria) {
        this.categoria = categoria;
    }
    
    public IdeiaStatus getEstado() {
        return this.estado;
    }


    public void setEstado(IdeiaStatus estado) {
        this.estado = estado;
    }

    public String  getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() { return imagem; }

    public void setImagem(String imagem) { this.imagem = imagem; }

   

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Set<Utilizador> getFavoritaPor() { return favoritaPor; }

    public Set<Utilizador> getGostadaPor() { return gostadaPor; }


}
