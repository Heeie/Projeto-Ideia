package Ideias.entities;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
public class Conversa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToMany @JoinTable(name = "conversa_participantes", joinColumns = @JoinColumn(name = "conversa_id"), inverseJoinColumns = @JoinColumn(name = "utilizador_id"))
    private Set<Utilizador> participantes = new HashSet<>();
    @OneToMany(mappedBy = "conversa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("criadoEm ASC") private Set<Mensagem> mensagens = new HashSet<>();

    public Long getId() { return id; }
    public Set<Utilizador> getParticipantes() { return participantes; }
    public Set<Mensagem> getMensagens() { return mensagens; }
}
