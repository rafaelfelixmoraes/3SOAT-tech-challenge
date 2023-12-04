package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.servicos.UsuarioService;
import br.com.tech.challenge.utils.PasswordUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

    @Mock
    private UsuarioService usuarioService;

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
        UsuarioDTO usuarioDTO = setUsuarioDto();
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

        UsuarioDTO usuarioDTO = setUsuarioDto();

        usuarioDTO.setUsuario("");
        usuarioDTO.setSenha("");
        usuarioDTO.setRole("");

        mockMvc.perform(post(ROTA_USUARIO)
                        .content(mapper.writeValueAsString(usuarioDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.containsInAnyOrder(
                        "O campo usuario é obrigatório.",
                        "O campo senha é obrigatório.",
                        "O campo role é obrigatório."
                )));
    }

    @DisplayName("Deve lançar uma exceção ao salvar um usuario já existente")
    @Test
    void shouldThrowExceptionWhenSavingUser() throws Exception {
        UsuarioDTO usuarioDTO = setUsuarioDto();

        mockMvc.perform(post(ROTA_USUARIO)
                        .content(mapper.writeValueAsString(usuarioDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private UsuarioDTO setUsuarioDto() {
        return UsuarioDTO.builder()
                .usuario("anamaria")
                .senha(PasswordUtils.encodePassword(SENHA))
                .role("ADMIN")
                .build();
    }

}
