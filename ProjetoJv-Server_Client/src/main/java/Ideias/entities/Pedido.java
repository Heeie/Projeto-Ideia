package Ideias.entities;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonBackReference;

import Ideias.enums.CategoriaIdeia;
import Ideias.enums.EstadoPedido;
import Ideias.enums.TypePedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Table(
    name = "pedido"
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private TypePedido tipo;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private EstadoPedido estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente pedinte;
    

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente julgador;
    
     public Pedido() {
        super();
    }

    public Pedido(TypePedido tipo, EstadoPedido estado, Cliente pedinte, Cliente julgador) {
        super();
    }

    public TypePedido getType(){
        return this.tipo;
    }
    
    public void setType(TypePedido t){
        this.tipo = t;
    }

    public EstadoPedido getEstado(){
        return this.estado;
    }
    
    public void setEstado(EstadoPedido p){
        this.estado = p;
    }

    public Cliente getPedinte(){
        return this.pedinte;
    }

    public void getPedinte( Cliente c){
        this.pedinte = c;
    }

    public Cliente getJulgador(){
        return this.julgador;
    }

    public void getJulgador( Cliente c){
        this.julgador = c;
    }
}

