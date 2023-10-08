package br.com.tech.challenge.api;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.domain.entidades.Cliente;
import br.com.tech.challenge.domain.entidades.FilaPedidos;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.enums.StatusPedido;
import br.com.tech.challenge.servicos.FilaPedidosService;
import br.com.tech.challenge.servicos.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private FilaPedidosService filaPedidosService;

    @Autowired
    private ObjectMapper mapper;

    private static final String ROTA_PEDIDOS = "/pedidos";

    @DisplayName("Deve salvar um pedido com sucesso")
    @Test
    void savePedidoSuccessTest() throws Exception {
        when(pedidoService.save(any())).thenReturn(setPedido());

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
        when(pedidoService.save(any())).thenThrow(new ObjectNotFoundException("Cliente não informado."));

        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(setPedidoDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve Lançar uma exceção ao salvar um pedido com Cliente inexistente.")
    @Test
    void savePedidoClientNonxistentTest() throws Exception {
        when(pedidoService.save(any()))
                .thenThrow(new ObjectNotFoundException("Cliente não encontrado " + setPedidoDTO().getId()));

        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(setPedidoDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Deve Lançar uma exceção ao salvar um pedido com produto inexistente")
    @Test
    void savePedidoProdutoInexistenteTest() throws Exception {
        when(pedidoService.save(any())).thenThrow(new ObjectNotFoundException("Produto não informado."));

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
        when(pedidoService.save(any()))
                .thenThrow(new ObjectNotFoundException("Produto não encontrado " + setPedidoDTO().getId()));

        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(setPedidoDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

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
                .produtos(List.of(setProduto()))
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
                .nome("Anthony Samuel Joaquim Teixeira")
                .email("anthony.samuel.teixeira@said.adv.br")
                .cpf("143.025.400-95")
                .build();
    }

}
