package br.com.tech.challenge.api;


import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.StatusPedidoDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Cliente;
import br.com.tech.challenge.domain.entidades.FilaPedidos;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.enums.StatusPedido;
import br.com.tech.challenge.servicos.FilaPedidosService;
import br.com.tech.challenge.servicos.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Mock
    private FilaPedidosService filaPedidosService;

    @Mock
    private PedidoService pedidoService;

    private static final String ROTA_PEDIDOS = "/pedidos";

    PedidoControllerTest() {
    }

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(equalTo(1))))
                .andExpect(jsonPath("$.valor_total", is(equalTo(10.0))))
                // Verifica o cliente
                .andExpect(jsonPath("$.cliente.id", is(equalTo(105))))
                .andExpect(jsonPath("$.cliente.nome", is(equalTo("Ana Maria Souza"))))
                .andExpect(jsonPath("$.cliente.cpf", is(equalTo("299.106.340-825"))))
                .andExpect(jsonPath("$.cliente.email", is(equalTo("ana.maria2@gmail.com"))))
                // Verifica a lista de produtos
                .andExpect(jsonPath("$.produtos", hasSize(1)))
                .andExpect(jsonPath("$.produtos[0].id", is(equalTo(101))))
                .andExpect(jsonPath("$.produtos[0].descricao", is(equalTo("Coca Cola"))))
                .andExpect(jsonPath("$.produtos[0].categoria.id", is(equalTo(2))))
                .andExpect(jsonPath("$.produtos[0].categoria.descricao", is(equalTo("Bebida"))))
                .andExpect(jsonPath("$.produtos[0].valor_unitario", is(equalTo(10.0))));
    }

    @DisplayName("Deve lançar uma exceção ao salvar um pedido com produto inexistente")
    @Test
    void shouldThrowExceptionWhenProdutoNonExistent() throws Exception {
        PedidoDTO pedidoDTO = PedidoDTO.builder()
                .senhaRetirada(123456)
                .produtos(Collections.emptyList())  // Lista vazia de produtos
                .valorTotal(BigDecimal.TEN)
                .statusPedido(StatusPedido.RECEBIDO)
                .build();


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

        mockMvc.perform(get(ROTA_PEDIDOS.concat("/fila"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @DisplayName("Deve listar a fila de pedidos vazia com sucesso")
    @Test
    void shouldListFilaPedidosEmptySuccess() throws Exception {

        final var pagina = String.valueOf(10);
        final var tamanho = String.valueOf(5);

        mockMvc.perform(get(ROTA_PEDIDOS.concat("/fila"))
                        .param("pagina", pagina)
                        .param("tamanho", tamanho)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }


    @DisplayName("Deve atualizar o status do pedido com sucesso")
    @Test
    void shouldUpdateStatusPedidoSuccess() throws Exception {
        StatusPedidoDTO statusPedidoDTO = new StatusPedidoDTO(StatusPedido.PRONTO);

        mockMvc.perform(put(ROTA_PEDIDOS.concat("/100/status"))
                        .content(mapper.writeValueAsString(statusPedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Deve listar os Pedidos com sucesso")
    @Test
    void shouldListPedidosSuccess() throws Exception {
        final var pagina = String.valueOf(0);
        final var tamanho = String.valueOf(5);


        mockMvc.perform(get(ROTA_PEDIDOS)
                        .param("pagina", pagina)
                        .param("tamanho", tamanho)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("Deve listar os pedidos vazios com sucesso")
    @Test
    void shouldListEmptyPedidosSuccess() throws Exception {
        final var queryParam = String.valueOf(100L);

        mockMvc.perform(get(ROTA_PEDIDOS)
                        .param("pagina", queryParam)
                        .param("tamanho", queryParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    private PedidoDTO setPedidoDTO() {
        return PedidoDTO.builder()
                .id(45L)
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
                .id(105L)
                .nome("Ana Maria Souza")
                .email("ana.maria2@gmail.com")
                .cpf("299.106.340-825")
                .build();
    }

    private ProdutoDTO setProdutoDTO() {
        return ProdutoDTO.builder()
                .id(101L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(10.00))
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


    private List<PedidoDTO> setPedidoList() {
        List<PedidoDTO> pedidoList = new ArrayList<>();

        PedidoDTO pedido1 = new PedidoDTO();
        pedido1.setId(1L);
        pedido1.setCliente(setClienteDTO());

        PedidoDTO pedido2 = new PedidoDTO();
        pedido2.setId(2L);
        pedido2.setCliente(setClienteDTO());

        pedidoList.add(pedido1);
        pedidoList.add(pedido2);

        return pedidoList;
    }

    private Page<PedidoDTO> createMockPage() {
        List<PedidoDTO> pedidos = List.of(setPedidoDTO());
        return new PageImpl<>(pedidos, PageRequest.of(0, 10), pedidos.size());
    }

    public static Pedido createMockPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setSenhaRetirada(123456);
        pedido.setCliente(createMockCliente());
        pedido.setProdutos(createMockProdutos());
        pedido.setValorTotal(new BigDecimal("100.00"));
        pedido.setStatusPedido(StatusPedido.RECEBIDO);
        pedido.setPagamento(null); // Você pode definir como necessário

        return pedido;
    }

    private static Cliente createMockCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Nome do Cliente");
        cliente.setCpf("123.456.789-00");
        cliente.setEmail("cliente@example.com");
        return cliente;
    }

    private static List<Produto> createMockProdutos() {
        List<Produto> produtos = new ArrayList<>();
        Produto produto1 = createMockProduto(1L, "Produto 1", new BigDecimal("10.00"));
        Produto produto2 = createMockProduto(2L, "Produto 2", new BigDecimal("20.00"));
        produtos.add(produto1);
        produtos.add(produto2);
        return produtos;
    }

    private static Produto createMockProduto(Long id, String descricao, BigDecimal valorUnitario) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setDescricao(descricao);
        produto.setCategoria(createMockCategoria());
        produto.setValorUnitario(valorUnitario);
        return produto;
    }

    private static Categoria createMockCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setDescricao("Categoria de Produtos");
        return categoria;
    }

}
