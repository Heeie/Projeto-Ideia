package main.Ideias.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;



@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Utilizador{
    
    public Admin() {
        super();
    }

    public Admin(String email, String nome, SubsUtilizador subscricao) {
        super(email, nome, subscricao);
    }

}
