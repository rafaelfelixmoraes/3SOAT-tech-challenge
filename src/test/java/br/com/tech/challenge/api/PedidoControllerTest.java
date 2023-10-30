package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.FilaPedidos;
import br.com.tech.challenge.domain.enums.StatusPedido;
import br.com.tech.challenge.servicos.FilaPedidosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private FilaPedidosService filaPedidosService;

    private static final String ROTA_PEDIDOS = "/pedidos";

    @AfterAll
    static void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @DisplayName("Deve salvar um pedido com sucesso")
    @Test
    void shouldSavePedidoSuccess() throws Exception {
        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(setPedidoDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("Deve lançar uma exceção ao salvar um pedido com Cliente não informado.")
    @Test
    void shouldThrowExceptionWhenClientNotInformed() throws Exception {
        var pedidoDTO = setPedidoDTO();
        pedidoDTO.setCliente(null);
        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(pedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve lançar uma exceção ao salvar um pedido com Cliente inexistente.")
    @Test
    void shouldThrowExceptionWhenClientNonExistent() throws Exception {
        var pedidoDTO = setPedidoDTO();
        pedidoDTO.setCliente(ClienteDTO.builder().id(100L).build());
        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(pedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve lançar uma exceção ao salvar um pedido com produto inexistente")
    @Test
    void shouldThrowExceptionWhenProdutoNonExistent() throws Exception {
        var pedidoDTO = setPedidoDTO();
        pedidoDTO.setProdutos(List.of(ProdutoDTO.builder().id(100L).build()));
        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(pedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve lançar uma exceção ao salvar um pedido com lista de produtos vazia")
    @Test
    void shouldThrowExceptionWhenProdutoEmptyList() throws Exception {
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
    void shouldListFilaPedidosSuccess() throws Exception {
        var listaPedidos = new PageImpl<>(Collections.singletonList(setFilaPedidos()));

        when(filaPedidosService.listFilaPedidos(anyInt(), anyInt())).thenReturn(listaPedidos);

        mockMvc.perform(get(ROTA_PEDIDOS.concat("/fila"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @DisplayName("Deve listar a fila de pedidos vazia com sucesso")
    @Test
    void shouldListFilaPedidosEmptySuccess() throws Exception {
        when(filaPedidosService.listFilaPedidos(anyInt(), anyInt())).thenReturn(Page.empty());

        mockMvc.perform(get(ROTA_PEDIDOS.concat("/fila"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    private PedidoDTO setPedidoDTO() {
        return PedidoDTO.builder()
                .id(1L)
                .senhaRetirada(123456)
                .cliente(setClienteDTO())
                .produtos(List.of(setProdutoDTO()))
                .valorTotal(BigDecimal.valueOf(5.00))
                .statusPedido(StatusPedido.RECEBIDO)
                .build();
    }

    private Categoria setCategoria() {
        return Categoria.builder()
                .id(2L)
                .descricao("Bebida")
                .build();
    }

    private ClienteDTO setClienteDTO() {
        return ClienteDTO.builder()
                .id(10L)
                .nome("Ana Maria")
                .email("ana.maria@gmail.com")
                .cpf("603.072.360-05")
                .build();
    }

    private ProdutoDTO setProdutoDTO() {
        return ProdutoDTO.builder()
                .id(10L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(5.00))
                .categoria(setCategoria())
                .build();
    }

    private FilaPedidos setFilaPedidos() {
        return FilaPedidos.builder()
                .senhaRetirada(123)
                .idCliente(1)
                .nomeCliente("Aline Oliveira")
                .statusPedido(StatusPedido.RECEBIDO.getDescricao())
                .build();
    }

}
