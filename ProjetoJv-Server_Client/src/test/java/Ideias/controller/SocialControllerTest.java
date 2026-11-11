package Ideias.controller;

import Ideias.entities.Cliente;
import Ideias.entities.Ideia;
import Ideias.enums.CategoriaIdeia;
import Ideias.repositories.ClienteRepository;
import Ideias.repositories.IdeiaRepository;
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
    Cliente author, other;
    Ideia idea;

    @BeforeEach void setup() {
        String suffix = java.util.UUID.randomUUID().toString();
        author = users.save(new Cliente("autor-" + suffix + "@social.test", "Autora", "password"));
        other = users.save(new Cliente("outro-" + suffix + "@social.test", "Outro", "password"));
        idea = ideas.save(new Ideia("Ideia social", CategoriaIdeia.ARTE, author));
    }

    @Test void socialAreasUsePersistentApiResources() throws Exception {
        mvc.perform(put("/api/utilizadores/{u}/favoritos/{i}", other.getId(), idea.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.favorito").value(true));
        mvc.perform(get("/api/utilizadores/{u}/favoritos", other.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(idea.getId()));
        mvc.perform(post("/api/ideias/{i}/comentarios", idea.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"autorId\":" + other.getId() + ",\"texto\":\"Comentário persistente\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.texto").value("Comentário persistente"));
        mvc.perform(get("/api/ideias/{i}/comentarios", idea.getId()).param("utilizadorId", author.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].autorNome").value("Outro"));
        mvc.perform(post("/api/grupos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Criadores\",\"descricao\":\"Grupo de teste\",\"criadorId\":" + author.getId() + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.membroAtual").value(true));
        mvc.perform(post("/api/amizades").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requerenteId\":" + author.getId() + ",\"destinatarioId\":" + other.getId() + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("PENDENTE"));
        mvc.perform(get("/api/utilizadores/{u}/notificacoes", other.getId()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[*].tipo", org.hamcrest.Matchers.hasItem("AMIZADE")));
        mvc.perform(delete("/api/ideias/{id}", idea.getId())).andExpect(status().isNoContent());
    }

    @Test void messagingAndPreferencesAreAvailable() throws Exception {
        String conversation = mvc.perform(post("/api/conversas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"utilizadorId\":" + author.getId() + ",\"contactoId\":" + other.getId() + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.contactoNome").value("Outro"))
                .andReturn().getResponse().getContentAsString();
        long conversationId = com.fasterxml.jackson.databind.json.JsonMapper.builder().build().readTree(conversation).get("id").asLong();
        mvc.perform(post("/api/conversas/{c}/mensagens", conversationId).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"remetenteId\":" + author.getId() + ",\"texto\":\"Olá\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.texto").value("Olá"));
        mvc.perform(get("/api/conversas/{c}/mensagens", conversationId).param("utilizadorId", other.getId().toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].remetenteNome").value("Autora"));
        mvc.perform(put("/api/utilizadores/{u}/preferencias", other.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"novasIdeias\":false,\"comentarios\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.novasIdeias").value(false));
    }
}
