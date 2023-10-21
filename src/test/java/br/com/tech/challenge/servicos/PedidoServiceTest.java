package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.ClienteRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.bd.repositorios.ProdutoRepository;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Cliente;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.domain.enums.StatusPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class PedidoServiceTest {

    private final PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ModelMapper mapper;

    PedidoServiceTest() {
        MockitoAnnotations.openMocks(this);
        pedidoService = new PedidoService(pedidoRepository, produtoRepository, clienteRepository, mapper);
    }

    @DisplayName("Deve criar um pedido com sucesso")
    @Test
    void shouldCreatePedidoSuccess() {

        var returnedPedido = setPedido();
        var returnedPedidoDTO = setPedidoDTO();

        when(pedidoRepository.save(any())).thenReturn(returnedPedido);
        when(clienteRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(produtoRepository.findById(any())).thenReturn(Optional.of(setProduto()));
        when(mapper.map(any(), any(Type.class))).thenReturn(Collections.singletonList(setProduto()));

        var pedido = pedidoService.save(returnedPedidoDTO);

        assertEquals(returnedPedido.getId(), pedido.getId());
        assertEquals(returnedPedido.getCliente(), pedido.getCliente());
        assertEquals(returnedPedido.getProdutos(), pedido.getProdutos());
        assertEquals(returnedPedido.getValorTotal(), pedido.getValorTotal());
        assertEquals(returnedPedido.getStatusPedido(), pedido.getStatusPedido());
        assertEquals(returnedPedido.getSenhaRetirada(), pedido.getSenhaRetirada());
        assertEquals(returnedPedido.getId().getClass(), pedido.getId().getClass());
        assertEquals(returnedPedido.getCliente().getClass(), pedido.getCliente().getClass());
        assertEquals(returnedPedido.getProdutos().getClass(), pedido.getProdutos().getClass());
        assertEquals(returnedPedido.getValorTotal().getClass(), pedido.getValorTotal().getClass());
        assertEquals(returnedPedido.getStatusPedido().getClass(), pedido.getStatusPedido().getClass());
        assertEquals(returnedPedido.getSenhaRetirada().getClass(), pedido.getSenhaRetirada().getClass());
        assertEquals(returnedPedido.getDataHora(), pedido.getDataHora());
    }

    @DisplayName("Deve lançar exceção ao criar um pedido com cliente não informado")
    @Test
    void shouldValidateNullClient() {
        try {
            var returnedPedidoDTO = setPedidoDTO();
            returnedPedidoDTO.setCliente(null);
            pedidoService.save(returnedPedidoDTO);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Cliente não informado.", e.getMessage());
        }
    }

    @DisplayName("Deve lançar exceção ao criar um pedido com cliente não encontrado")
    @Test
    void shouldValidateExistingClient() {
        var returnedPedidoDTO = setPedidoDTO();
        try {
            when(clienteRepository.existsById(any())).thenReturn(Boolean.FALSE);
            pedidoService.save(returnedPedidoDTO);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Cliente não encontrado " + returnedPedidoDTO.getCliente().getId(), e.getMessage());
        }
    }

@DisplayName("Deve lançar exceção ao criar um pedido com lista de produtos vazia")
@Test
void shouldValidateEmptyListProductsOrder() {
        try {
            var returnedPedidoDTO = setPedidoDTO();
            when(clienteRepository.existsById(any())).thenReturn(Boolean.TRUE);
            returnedPedidoDTO.setProdutos(List.of());
            pedidoService.save(returnedPedidoDTO);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Lista de produtos vazia", e.getMessage());
        }
}

    @DisplayName("Deve lançar exceção ao criar um pedido com produto não encontrado")
    @Test
    void shouldValidateProductExisting() {
        var returnedPedidoDTO = setPedidoDTO();
        try {
            when(clienteRepository.existsById(any())).thenReturn(Boolean.TRUE);
            when(produtoRepository.findById(any())).thenReturn(Optional.empty());
            when(mapper.map(any(), any(Type.class))).thenReturn(Collections.singletonList(setProduto()));
            pedidoService.save(returnedPedidoDTO);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Produto não encontrado " + returnedPedidoDTO
                    .getProdutos().get(0).getId(), e.getMessage());
        }
    }

    private Pedido setPedido() {
        return Pedido.builder()
                .id(1L)
                .senhaRetirada(123456)
                .cliente(setCliente())
                .produtos(List.of(setProduto()))
                .valorTotal(BigDecimal.valueOf(5.00))
                .statusPedido(StatusPedido.RECEBIDO)
                .dataHora(LocalDateTime.now())
                .build();
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

    private ProdutoDTO setProdutoDTO() {
        return ProdutoDTO.builder()
                .id(1L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(5.00))
                .categoria(setCategoria())
                .build();
    }

    private ClienteDTO setClienteDTO() {
        return ClienteDTO.builder()
                .id(1L)
                .nome("Anthony Samuel Joaquim Teixeira")
                .email("anthony.samuel.teixeira@said.adv.br")
                .cpf("143.025.400-95")
                .build();
    }

}
