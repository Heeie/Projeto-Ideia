package Ideias.entities;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;


@Entity
@DiscriminatorValue("CLIENTE")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo" // campo que vai ser usado no JSON para identificar o subtipo
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Cliente.class, name = "CLIENTE")
})
public class Cliente extends Utilizador {

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ideia> ideiasCriadas = new ArrayList<>();

    public Cliente() {
        super();
    }

    public Cliente(String email, String nome, String password) {
        super(email, nome, password);
    }

    public List<Ideia> getHistorico() {
        return ideiasCriadas;
    }

    public void setHistorico(List<Ideia> historico) {
    	this.ideiasCriadas = historico;
    }

}
