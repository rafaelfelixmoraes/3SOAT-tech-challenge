package br.com.tech.challenge.api;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterAll
    static void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    private static final String ROTA_PAGAMENTOS = "/pagamentos/pedido/{idPedido}/checkout";

    @DisplayName("Deve fazer o checkout com sucesso")
    @Test
    void shouldDoCheckoutSuccess() throws Exception {
        final Long idPedido = 100L;
        mockMvc.perform(post(ROTA_PAGAMENTOS, idPedido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
