package br.com.tech.challenge.api;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.api.exception.StatusPedidoInvalidoException;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.StatusPedidoDTO;
import br.com.tech.challenge.domain.entidades.*;
import br.com.tech.challenge.domain.enums.StatusPedido;
import br.com.tech.challenge.servicos.FilaPedidosService;
import br.com.tech.challenge.servicos.PedidoService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @MockBean
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
        PedidoDTO pedidoDTO = setPedidoDTO();

        Pedido pedido = createMockPedido();

        // Configuração de retorno simulado do serviço
        when(pedidoService.save(any(PedidoDTO.class))).thenReturn(pedido);

        mockMvc.perform(post(ROTA_PEDIDOS)
                        .content(mapper.writeValueAsString(pedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.senha_retirada").value(pedido.getSenhaRetirada()))
                .andExpect(jsonPath("$.cliente.id").value(pedido.getCliente().getId()))
                .andExpect(jsonPath("$.cliente.nome").value(pedido.getCliente().getNome()))
                .andExpect(jsonPath("$.cliente.cpf").value(pedido.getCliente().getCpf()))
                .andExpect(jsonPath("$.cliente.email").value(pedido.getCliente().getEmail()))
                .andExpect(jsonPath("$.produtos[0].id").value(pedido.getProdutos().get(0).getId()))
                .andExpect(jsonPath("$.produtos[0].descricao").value(pedido.getProdutos().get(0).getDescricao()))
                .andExpect(jsonPath("$.produtos[0].categoria.id").value(pedido.getProdutos().get(0).getCategoria().getId()))
                .andExpect(jsonPath("$.produtos[0].categoria.descricao").value(pedido.getProdutos().get(0).getCategoria().getDescricao()))
                .andExpect(jsonPath("$.produtos[0].valor_unitario").value(pedido.getProdutos().get(0).getValorUnitario().doubleValue()))
                .andExpect(jsonPath("$.valor_total").value(pedido.getValorTotal().doubleValue()))
                .andExpect(jsonPath("$.status_pedido").value(pedido.getStatusPedido().toString()));
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

        // Configuração de exceção simulada no serviço
        doThrow(new ObjectNotFoundException("A lista de produtos não pode estar vazia"))
                .when(pedidoService).save(any(PedidoDTO.class));

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

        when(filaPedidosService.listaFilaPedidos(anyInt(), anyInt())).thenReturn(listaPedidos);

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
        when(filaPedidosService.listaFilaPedidos(anyInt(), anyInt())).thenReturn(Page.empty());

        mockMvc.perform(get(ROTA_PEDIDOS.concat("/fila"))
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

        PedidoDTO pedidoDTO = new PedidoDTO();
        doReturn(pedidoDTO).when(pedidoService).updateStatus(any(Long.class), any(StatusPedidoDTO.class));

        mockMvc.perform(put(ROTA_PEDIDOS.concat("/1/status"))
                        .content(mapper.writeValueAsString(statusPedidoDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Deve listar os Pedidos com sucesso")
    @Test
    void shouldListPedidosSuccess() throws Exception {

        Page<PedidoDTO> page = createMockPage();

        when(pedidoService.list(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/pedidos")
                        .param("pagina", "0")
                        .param("tamanho", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].senha_retirada", is(123456)))
                .andExpect(jsonPath("$[0].valor_total", is(5.0)))
                .andExpect(jsonPath("$[0].status_pedido", is("RECEBIDO")))
                .andExpect(jsonPath("$[0].cliente.id", is(10)))
                .andExpect(jsonPath("$[0].cliente.nome", is("Ana Maria")))
                .andExpect(jsonPath("$[0].cliente.cpf", is("603.072.360-05")))
                .andExpect(jsonPath("$[0].cliente.email", is("ana.maria@gmail.com")))
                .andExpect(jsonPath("$[0].produtos[0].id", is(10)))
                .andExpect(jsonPath("$[0].produtos[0].descricao", is("Coca Cola")))
                .andExpect(jsonPath("$[0].produtos[0].categoria.id", is(2)))
                .andExpect(jsonPath("$[0].produtos[0].categoria.descricao", is("Bebida")))
                .andExpect(jsonPath("$[0].produtos[0].valor_unitario", is(5.0)));

    }

    @DisplayName("Deve listar os pedidos vazios com sucesso")
    @Test
    void shouldListEmptyPedidosSuccess() throws Exception {
        final var queryParam = String.valueOf(100L);


        when(pedidoService.list(anyInt(), anyInt())).thenReturn(Page.empty());

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
                .nomeCliente("Cliente Teste")
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
