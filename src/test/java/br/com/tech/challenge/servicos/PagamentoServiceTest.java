package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.client.MercadoPagoClient;
import br.com.tech.challenge.bd.repositorios.PagamentoRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.entidades.Pagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PagamentoServiceTest {

    private final PagamentoService pagamentoService;

    private PagamentoRepository pagamentoRepository;

    private MercadoPagoClient mercadoPagoClient;

    private PedidoRepository pedidoRepository;

    private ProdutoService produtoService;

    PagamentoServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.pagamentoService = new PagamentoService(
                pagamentoRepository, mercadoPagoClient,
                pedidoRepository, produtoService
        );
    }

}
