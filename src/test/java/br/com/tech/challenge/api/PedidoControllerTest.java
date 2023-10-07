package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.entidades.FilaPedidos;
import br.com.tech.challenge.domain.enums.StatusPedido;
import br.com.tech.challenge.servicos.FilaPedidosService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilaPedidosService filaPedidosService;

    private static final String ROTA_PEDIDOS = "/pedidos";

    @DisplayName("Deve listar a fila de pedidos com sucesso")
    @Test
    void listFilaPedidosSuccess() throws Exception {
        Mockito.when(filaPedidosService.listaFilaPedidos()).thenReturn(setFilaPedidos());

        mockMvc.perform(get(ROTA_PEDIDOS.concat("/fila"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("Deve listar a fila de pedidos vazia com sucesso")
    @Test
    void listFilaPedidosVaziaSuccess() throws Exception {
        Mockito.when(filaPedidosService.listaFilaPedidos()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(ROTA_PEDIDOS.concat("/fila"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private List<FilaPedidos> setFilaPedidos() {
        var filaPedidos = FilaPedidos.builder()
                .senhaRetirada(RandomUtils.nextInt())
                .nomeCliente("Teste Fila Pedidos")
                .statusPedido(StatusPedido.FINALIZADO.getDescricao())
                .build();

        return Collections.singletonList(filaPedidos);
    }
}
