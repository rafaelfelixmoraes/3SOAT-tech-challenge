package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Cliente;
import br.com.tech.challenge.domain.entidades.FilaPedidos;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.domain.enums.StatusPedido;
import br.com.tech.challenge.servicos.FilaPedidosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilaPedidosService filaPedidosService;

    @Autowired
    private ObjectMapper mapper;

    private static final String ROTA_PEDIDOS = "/pedidos";

    @DisplayName("Deve salvar um pedido com sucesso")
    @Test
    void savePedidoSuccessTest() throws Exception {

        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(setPedido()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @DisplayName("Deve Lançar uma exceção ao salvar um pedido com Cliente não informado.")
    @Test
    void savePedidoClientNotInformedTest() throws Exception {
        var pedidoDTO = setPedidoDTO();
        pedidoDTO.setCliente(null);
        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(pedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve Lançar uma exceção ao salvar um pedido com Cliente inexistente.")
    @Test
    void savePedidoClientNonxistentTest() throws Exception {
        var pedidoDTO = setPedidoDTO();
        pedidoDTO.setCliente(Cliente.builder().id(10L).build());
        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(pedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve Lançar uma exceção ao salvar um pedido com produto inexistente")
    @Test
    void savePedidoProdutoInexistenteTest() throws Exception {

        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(setPedidoDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve Lançar uma exceção ao salvar um pedido com lista de produtos vazia")
    @Test
    void savePedidoProdutoEmptyListTest() throws Exception {
        var pedidoDTO = setPedidoDTO();
        pedidoDTO.setProdutos(Collections.emptyList());
        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(pedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve listar a fila de pedidos com sucesso")
    @Test
    void listFilaPedidosSuccess() throws Exception {

        mockMvc.perform(get(ROTA_PEDIDOS.concat("/fila"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Deve listar a fila de pedidos vazia com sucesso")
    @Test
    void listFilaPedidosVaziaSuccess() throws Exception {

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


    private Pedido setPedido() {
        return Pedido.builder()
                .id(1L)
                .senhaRetirada(123456)
                .cliente(setCliente())
                .produtos(List.of(setProduto()))
                .valorTotal(BigDecimal.valueOf(5.00))
                .statusPedido(StatusPedido.RECEBIDO)
                .build();
    }

    private PedidoDTO setPedidoDTO() {
        return PedidoDTO.builder()
                .id(1L)
                .senhaRetirada(123456)
                .cliente(setCliente())
                .produtos(List.of(Produto.builder().id(10L).build()))
                .valorTotal(BigDecimal.valueOf(5.00))
                .statusPedido(StatusPedido.RECEBIDO)
                .build();
    }

    private Produto setProduto() {
        return Produto.builder()
                .id(1L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(5.00))
                .categoria(setCategoria())
                .build();
    }

    private Categoria setCategoria() {
        return Categoria.builder()
                .id(2L)
                .descricao("Bebida")
                .build();
    }

    private Cliente setCliente() {
        return Cliente.builder()
                .id(1L)
                .nome("Ana Maria")
                .email("ana.maria@gmail.com")
                .cpf("143.025.400-95")
                .build();
    }

}
