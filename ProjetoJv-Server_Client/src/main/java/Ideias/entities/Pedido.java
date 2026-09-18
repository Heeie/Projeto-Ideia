package Ideias.entities;



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

import jakarta.persistence.InheritanceType;


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

    @Column( nullable = false)
    private Long pedinte;
    
    @Column( nullable = false)
    private Long julgador;
    
     public Pedido() {}


    public Pedido(TypePedido tipo, EstadoPedido estado, Long pedinte, Long julgador) {
        this.tipo = tipo;
        this.estado = estado;
        this.pedinte = pedinte;
        this.julgador = julgador;
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

    public Long getPedinte(){
        return this.pedinte;
    }

    public void setPedinte( Long c){
        this.pedinte = c;
    }

    public Long getJulgador(){
        return this.julgador;
    }

    public void setJulgador( Long c){
        this.julgador = c;
    }


    public Long getId() {
        return this.id;
    }
}

