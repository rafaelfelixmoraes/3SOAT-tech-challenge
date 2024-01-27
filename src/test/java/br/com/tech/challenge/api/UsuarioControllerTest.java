package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.CredencialDTO;
import br.com.tech.challenge.domain.dto.TokenDTO;
import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.domain.enums.Role;
import br.com.tech.challenge.utils.PasswordUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String SENHA = "123456";
    private static final String ROTA_USUARIO = "/usuarios";

    @AfterAll
    static void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @DisplayName("Deve salvar um usuario com sucesso")
    @Test
    void shouldSaveUsuarioSuccess() throws Exception {
        UsuarioDTO usuarioDTO = setUsuarioDTO();
        usuarioDTO.setUsuario("admin");

        mockMvc.perform(post(ROTA_USUARIO)
                        .content(mapper.writeValueAsString(usuarioDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.usuario").value(usuarioDTO.getUsuario()));

    }

    @DisplayName("Deve retornar erro ao criar um usuario inválido")
    @Test
    void shouldReturnErrorForInvalidUser() throws Exception {

        UsuarioDTO usuarioDTO = setUsuarioDTO();

        usuarioDTO.setUsuario("");
        usuarioDTO.setSenha("");
        usuarioDTO.setRole(null);

        mockMvc.perform(post(ROTA_USUARIO)
                        .content(mapper.writeValueAsString(usuarioDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.containsInAnyOrder(
                        "O campo senha é obrigatório.",
                        "O campo usuario é obrigatório.",
                        "O campo role é obrigatório."
                )));
    }

    @DisplayName("Deve autenticar o usuário (gerar token)")
    @Test
    void shouldAuthenticateUser() throws Exception {
        CredencialDTO credencialDTO = setCredencialDTO();

        MvcResult mvcResult = mockMvc.perform(post(ROTA_USUARIO + "/auth")
                        .content(mapper.writeValueAsString(credencialDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO tokenDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), TokenDTO.class);
        assertEquals("root", tokenDTO.getNomeUsuario());
        assertNotNull(tokenDTO.getToken());
    }

    private CredencialDTO setCredencialDTO() {
        return CredencialDTO.builder()
                .nomeUsuario("root")
                .senha("devrise@2023")
                .build();
    }

    private UsuarioDTO setUsuarioDTO() {
        return UsuarioDTO.builder()
                .usuario("anamaria")
                .senha(PasswordUtils.encodePassword(SENHA))
                .role(Role.ADMIN)
                .build();
    }

}
