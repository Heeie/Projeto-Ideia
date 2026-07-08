package Ideias.controller;

import Ideias.entities.Cliente;
import Ideias.entities.Utilizador;
import Ideias.repositories.UtilizadorRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UtilizadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    private Cliente cliente;

    @BeforeEach
    void setup() {

        cliente = new Cliente(
                "teste@exemplo.com",
                "João",
                "123456"
        );

        cliente = utilizadorRepository.save(cliente);
    }

    @Test
    void testCriar() throws Exception {

        mockMvc.perform(post("/api/utilizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email":"novo@exemplo.com",
                                    "nome":"Maria",
                                    "password":"abc123",
                                    "tipo":"CLIENTE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.email").value("novo@exemplo.com"));
    }

    @Test
    void testAtualizar() throws Exception {

        mockMvc.perform(put("/api/utilizadores/" + cliente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email":"novo@exemplo.com",
                                    "nome":"João Atualizado",
                                    "password":"novaPassword",
                                    "tipo":"CLIENTE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"))
                .andExpect(jsonPath("$.email").value("novo@exemplo.com"));
    }

    @Test
    void testListarTodos() throws Exception {

        mockMvc.perform(get("/api/utilizadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    void testDetalhes() throws Exception {

        mockMvc.perform(get("/api/utilizadores/" + cliente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("teste@exemplo.com"));
    }

    @Test
    void testRemover() throws Exception {

        mockMvc.perform(delete("/api/utilizadores/" + cliente.getId()))
                .andExpect(status().isNoContent());

        List<Utilizador> lista = utilizadorRepository.findAll();

        assertTrue(lista.isEmpty());
    }
}