package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.client.MercadoPagoClient;
import br.com.tech.challenge.bd.repositorios.PagamentoRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.entidades.*;
import br.com.tech.challenge.domain.enums.StatusPagamento;
import br.com.tech.challenge.domain.enums.StatusPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PagamentoServiceTest {

    private final PagamentoService pagamentoService;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private MercadoPagoClient mercadoPagoClient;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    PagamentoServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.pagamentoService = new PagamentoService(
                pagamentoRepository, mercadoPagoClient,
                pedidoRepository, produtoService
        );
    }

    @DisplayName("Deve criar um pagamento com sucesso")
    @Test
    void shouldCreatePagamentoSuccess() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var pedido = setPedido();
        var pagamento = setPagamento(clock);

        when(pagamentoRepository.save(any())).thenReturn(pagamento);

        var returnedPagamento = pagamentoService.save(pedido);

        assertEquals(1L, returnedPagamento.getId());
        assertEquals(1L, returnedPagamento.getPedido().getId());
        assertEquals(LocalDateTime.now(clock), returnedPagamento.getDataHoraPagamento());
        assertEquals(new BigDecimal("10.00"), returnedPagamento.getValorTotal());
        assertNull(returnedPagamento.getQrData());
        assertEquals(StatusPagamento.AGUARDANDO_PAGAMENTO, returnedPagamento.getStatusPagamento());
    }

    @DisplayName("Deve encontrar o pagamento pelo id do pedido")
    @Test
    void shouldFindPagamentoByPedidoId() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        final Long idPedido = 1L;

        when(pagamentoRepository.findPagamentoByPedidoId(idPedido)).thenReturn(Optional.of(setPagamento(clock)));

        var returnedPagamento = pagamentoService.findPagamentoByPedidoId(idPedido);

        assertEquals(1L, returnedPagamento.getId());
        assertEquals(1L, returnedPagamento.getPedido().getId());
        assertEquals(LocalDateTime.now(clock), returnedPagamento.getDataHoraPagamento());
        assertEquals(new BigDecimal("10.00"), returnedPagamento.getValorTotal());
        assertNull(returnedPagamento.getQrData());
        assertEquals(StatusPagamento.AGUARDANDO_PAGAMENTO, returnedPagamento.getStatusPagamento());
    }

    @DisplayName("Deve fazer o checkout")
    @Test
    void shouldDoCheckout() {
        var clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        var pagamento = setPagamento(clock);
        var pedido = setPedido();

        pedido = pedido.toBuilder().statusPedido(StatusPedido.EM_PREPARACAO).build();
        pagamento = pagamento.toBuilder().pedido(pedido).build();

        final Long idPedido = 1L;

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));
        when(pagamentoRepository.findPagamentoByPedidoId(idPedido)).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any())).thenReturn(pagamento.toBuilder()
                .statusPagamento(StatusPagamento.PAGO)
                .dataHoraPagamento(LocalDateTime.now(clock))
                .build()
        );

        var returnedPagamento = pagamentoService.checkout(idPedido);
        assertEquals(StatusPagamento.PAGO, returnedPagamento.getStatusPagamento());
        assertNotNull(returnedPagamento.getDataHoraPagamento());
        assertEquals(StatusPedido.EM_PREPARACAO, returnedPagamento.getPedido().getStatusPedido());
    }

    private Pagamento setPagamento(Clock clock) {
        return Pagamento.builder()
                .id(1L)
                .pedido(setPedido())
                .dataHoraPagamento(LocalDateTime.now(clock))
                .valorTotal(new BigDecimal("10.00"))
                .qrData(null)
                .statusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO)
                .build();
    }

    private Pedido setPedido() {
        return Pedido.builder()
                .id(1L)
                .senhaRetirada(123456)
                .cliente(setCliente())
                .produtos(List.of(setProduto()))
                .valorTotal(BigDecimal.valueOf(10.00))
                .statusPedido(StatusPedido.RECEBIDO)
                .dataHora(LocalDateTime.now())
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
