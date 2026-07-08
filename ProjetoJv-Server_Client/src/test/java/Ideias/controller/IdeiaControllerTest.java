package Ideias.controller;

import Ideias.dto.IdeiaDto;
import Ideias.entities.Cliente;
import Ideias.entities.Ideia;
import Ideias.enums.CategoriaIdeia;
import Ideias.enums.IdeiaStatus;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IdeiaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IdeiaRepository ideiaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;
    private Ideia ideia;

    @BeforeEach
    void setup() {

        cliente = new Cliente(
                "cliente@teste.com",
                "João",
                "123456"
        );

        cliente = clienteRepository.save(cliente);

        ideia = new Ideia(
                "Ideia Inicial",
                CategoriaIdeia.COZINHA,
                cliente
        );

        ideia.setDescricao("Descrição inicial");
        ideia.setEstado(IdeiaStatus.PRIVADA);

        ideia = ideiaRepository.save(ideia);
    }

    @Test
    void testCriar() throws Exception {

        mockMvc.perform(post("/api/ideias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome":"Nova Ideia",
                                  "descricao":"Descrição",
                                  "categoria":"LAZER",
                                  "estado":"PUBLICA",
                                  "clienteId":%d
                                }
                                """.formatted(cliente.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nova Ideia"));
    }

    @Test
    void testAtualizar() throws Exception {

        mockMvc.perform(put("/api/ideias/" + ideia.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome":"Ideia Atualizada",
                                  "descricao":"Nova descrição",
                                  "categoria":"DESPORTO",
                                  "estado":"LIMITADA",
                                  "clienteId":%d
                                }
                                """.formatted(cliente.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ideia Atualizada"))
                .andExpect(jsonPath("$.descricao").value("Nova descrição"));
    }

    @Test
    void testListarTodos() throws Exception {

        mockMvc.perform(get("/api/ideias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Ideia Inicial"));
    }

    @Test
    void testDetalhes() throws Exception {

        mockMvc.perform(get("/api/ideias/" + ideia.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ideia Inicial"));
    }

    @Test
    void testRemover() throws Exception {

        mockMvc.perform(delete("/api/ideias/" + ideia.getId()))
                .andExpect(status().isNoContent());

        List<Ideia> lista = ideiaRepository.findAll();

        assertTrue(lista.isEmpty());
    }
}