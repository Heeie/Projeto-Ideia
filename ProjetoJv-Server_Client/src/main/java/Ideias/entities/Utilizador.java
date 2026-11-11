package Ideias.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;



@Entity
@Table(
    name = "utilizador"
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Utilizador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 254)
    private String email;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, length = 255)
    private String password;


    @ManyToMany
    @JoinTable(name = "utilizador_favoritos",
        joinColumns = @JoinColumn(name = "utilizador_id"),
        inverseJoinColumns = @JoinColumn(name = "ideia_id"))
    private Set<Ideia> ideiasFav = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "utilizador_gostos_ideias",
        joinColumns = @JoinColumn(name = "utilizador_id"),
        inverseJoinColumns = @JoinColumn(name = "ideia_id"))
    private Set<Ideia> ideiasGostadas = new HashSet<>();
    

    public Utilizador() {}

    public Utilizador(String email, String nome, String password) {
        this.email = email;
        this.nome = nome;
        this.password = password;
    }
    
    public Set<Ideia> getIdeiasFav() {
        return ideiasFav;
    }

    public void setIdeiasFav(Set<Ideia> ideiasFav) {
        this.ideiasFav = ideiasFav;
    }

    public Set<Ideia> getIdeiasGostadas() { return ideiasGostadas; }

    public void setIdeiasGostadas(Set<Ideia> ideiasGostadas) { this.ideiasGostadas = ideiasGostadas; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        this.password = p;
    }



}
