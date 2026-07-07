package Ideias.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Utilizador{
    
    public Admin() {
        super();
    }

    public Admin(String email, String nome, String password) {
        super(email, nome, password);
    }

}
