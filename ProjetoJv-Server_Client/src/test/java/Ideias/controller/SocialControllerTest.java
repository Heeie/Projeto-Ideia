package Ideias.controller;

import Ideias.entities.Cliente;
import Ideias.entities.Ideia;
import Ideias.enums.CategoriaIdeia;
import Ideias.enums.IdeiaStatus;
import Ideias.repositories.ClienteRepository;
import Ideias.repositories.IdeiaRepository;
import Ideias.security.ApiTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SocialControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ClienteRepository users;
    @Autowired IdeiaRepository ideas;
    @Autowired ApiTokenService tokens;
    Cliente author, other;
    Ideia idea;

    @BeforeEach void setup() {
        String suffix = java.util.UUID.randomUUID().toString();
        author = users.save(new Cliente("autor-" + suffix + "@social.test", "Autora", "password"));
        other = users.save(new Cliente("outro-" + suffix + "@social.test", "Outro", "password"));
        idea = new Ideia("Ideia social", CategoriaIdeia.ARTE, author);
        idea.setEstado(IdeiaStatus.PUBLICA);
        idea = ideas.save(idea);
    }

    @Test void socialAreasUsePersistentApiResources() throws Exception {
        mvc.perform(put("/api/utilizadores/{u}/favoritos/{i}", other.getId(), idea.getId()).header("Authorization", auth(other)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.favorito").value(true));
        mvc.perform(get("/api/utilizadores/{u}/favoritos", other.getId()).header("Authorization", auth(other)))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(idea.getId()));
        mvc.perform(post("/api/ideias/{i}/comentarios", idea.getId()).header("Authorization", auth(other)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"autorId\":" + other.getId() + ",\"texto\":\"Comentário persistente\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.texto").value("Comentário persistente"));
        mvc.perform(get("/api/ideias/{i}/comentarios", idea.getId()).header("Authorization", auth(author)).param("utilizadorId", author.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].autorNome").value("Outro"));
        mvc.perform(post("/api/grupos").header("Authorization", auth(author)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Criadores\",\"descricao\":\"Grupo de teste\",\"criadorId\":" + author.getId() + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.membroAtual").value(true));
        mvc.perform(post("/api/amizades").header("Authorization", auth(author)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requerenteId\":" + author.getId() + ",\"destinatarioId\":" + other.getId() + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("PENDENTE"));
        mvc.perform(get("/api/utilizadores/{u}/notificacoes", other.getId()).header("Authorization", auth(other)))
                .andExpect(status().isOk()).andExpect(jsonPath("$[*].tipo", org.hamcrest.Matchers.hasItem("AMIZADE")));
        mvc.perform(delete("/api/ideias/{id}", idea.getId()).header("Authorization", auth(author))).andExpect(status().isNoContent());
    }

    @Test void messagingAndPreferencesAreAvailable() throws Exception {
        String conversation = mvc.perform(post("/api/conversas").header("Authorization", auth(author)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"utilizadorId\":" + author.getId() + ",\"contactoId\":" + other.getId() + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.contactoNome").value("Outro"))
                .andReturn().getResponse().getContentAsString();
        long conversationId = com.fasterxml.jackson.databind.json.JsonMapper.builder().build().readTree(conversation).get("id").asLong();
        mvc.perform(post("/api/conversas/{c}/mensagens", conversationId).header("Authorization", auth(author)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"remetenteId\":" + author.getId() + ",\"texto\":\"Olá\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.texto").value("Olá"));
        mvc.perform(get("/api/conversas/{c}/mensagens", conversationId).header("Authorization", auth(other)).param("utilizadorId", other.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].remetenteNome").value("Autora"));
        mvc.perform(put("/api/utilizadores/{u}/preferencias", other.getId()).header("Authorization", auth(other)).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"novasIdeias\":false,\"comentarios\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.novasIdeias").value(false));
    }

    private String auth(Cliente user) { return "Bearer " + tokens.issue(user.getId()); }
}
