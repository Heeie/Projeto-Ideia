package Ideias.entities;

import Ideias.entities.Cliente;
import Ideias.enums.CategoriaIdeia;
import Ideias.enums.IdeiaStatus;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import Ideias.enums.IdeiaStatus;

@Entity
public class Ideia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaIdeia categoria;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdeiaStatus estado;

    @Column(nullable = true)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    
    @ManyToOne
    @JoinColumn(name = "utilizador_favorito_id")
    private Utilizador utilizador;
    
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

   

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }


}
