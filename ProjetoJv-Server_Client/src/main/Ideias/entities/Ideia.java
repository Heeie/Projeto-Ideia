package main.Ideias.entities;

import main.Client;
import main.Ideias.enums.CategoriaIdeia;

@Entity
public class Ideia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaIdeia categoria = CategoriaIdeia.COZINHA;

    @Column(nullable = true)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Ideia() {}

    public Ideia(String nome, CategoriaIdeia categoria, Cliente cliente) {
        this.nome = nome;
        this.categoria = categoria;
        this.cliente = cliente;
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

    public String  getReservadoPor() {
        return this.descricao;
    }

    public void setReservadoPor(String descricao) {
        this.descricao = descricao;
    }

   

    public Cliente  getEstacao() {
        return this.cliente;
    }

    public void setEstacao(Cliente cliente) {
        this.cliente = cliente;
    }

}
