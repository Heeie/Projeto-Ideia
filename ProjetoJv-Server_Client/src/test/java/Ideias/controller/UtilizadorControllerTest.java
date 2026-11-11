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

    @Test
    void registarEIniciarSessaoSemExporPassword() throws Exception {
        mockMvc.perform(post("/api/auth/registo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Maria","email":"maria@exemplo.com","password":"password123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("maria@exemplo.com"))
                .andExpect(jsonPath("$.password").doesNotExist());

        String passwordGuardada = utilizadorRepository.findByEmail("maria@exemplo.com")
                .orElseThrow().getPassword();
        org.junit.jupiter.api.Assertions.assertNotEquals("password123", passwordGuardada);
        org.junit.jupiter.api.Assertions.assertTrue(passwordGuardada.startsWith("pbkdf2-sha256$"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"MARIA@EXEMPLO.COM","password":"password123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void atualizarPerfilSemSubstituirCredenciais() throws Exception {
        mockMvc.perform(put("/api/utilizadores/" + cliente.getId() + "/perfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"João Novo","email":"joao.novo@exemplo.com"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Novo"))
                .andExpect(jsonPath("$.password").doesNotExist());

        org.junit.jupiter.api.Assertions.assertEquals("123456",
                utilizadorRepository.findById(cliente.getId()).orElseThrow().getPassword());
    }

    @Test
    void permitirCorsParaFrontendLocal() throws Exception {
        mockMvc.perform(options("/api/ideias")
                        .header("Origin", "http://localhost:5500")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5500"));
    }
}
