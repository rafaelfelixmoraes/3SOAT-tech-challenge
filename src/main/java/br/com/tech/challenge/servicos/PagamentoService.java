package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.client.MercadoPagoClient;
import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.PagamentoRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.dto.external.CashOutDTO;
import br.com.tech.challenge.domain.dto.external.ItemDTO;
import br.com.tech.challenge.domain.dto.external.MercadoPagoRequestDTO;
import br.com.tech.challenge.domain.dto.external.MercadoPagoResponseDTO;
import br.com.tech.challenge.domain.entidades.Pagamento;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.enums.StatusPagamento;
import br.com.tech.challenge.domain.enums.StatusPedido;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    private final MercadoPagoClient mercadoPagoClient;

    private final PedidoRepository pedidoRepository;

    private final ProdutoService produtoService;

    @Transactional
    public Pagamento save(Pedido pedido) {
        log.info("Montando pagamento antes de salvar");
        var pagamento = Pagamento.builder()
                .pedido(pedido)
                .valorTotal(pedido.getValorTotal())
                .statusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO)
                .build();
        log.info("Salvando pagamento do pedido {}", pedido.getId());
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento findPagamentoByPedidoId(Long idPedido) {
        log.info("Buscando pagamento por id do pedido {}", idPedido);
        return pagamentoRepository.findPagamentoByPedidoId(idPedido)
                .orElseThrow(() -> new ObjectNotFoundException("Pagamento não encontrado."));
    }

    @Transactional
    @Generated
    public MercadoPagoResponseDTO generateQRCode(Long idPedido) {
        var pedido = getPedido(idPedido);
        var requestDTO = buildMercadoPagoRequestDTO(pedido);
        log.info("Chamando client para gerar QR Code");
        var responseDTO = mercadoPagoClient.generateQRCode(requestDTO);

        var pagamento = findPagamentoByPedidoId(pedido.getId());
        pagamento.setQrData(responseDTO.getQrData());

        log.info("Salvando pagamento");
        pagamentoRepository.save(pagamento);
        return responseDTO;
    }

    @Transactional
    public Pagamento checkout(Long idPedido) {
        log.info("Checkout de pedido {}", idPedido);
        var pedido = getPedido(idPedido);
        var pagamento = findPagamentoByPedidoId(pedido.getId());

        log.info("Alterando status do pagamento para PAGO");
        pagamento.setStatusPagamento(StatusPagamento.PAGO);
        pagamento.setDataHoraPagamento(LocalDateTime.now());

        log.info("Alterando status do pedido para EM_PREPARACAO");
        pedido.setStatusPedido(StatusPedido.EM_PREPARACAO);
        pedidoRepository.save(pedido);
        return pagamentoRepository.save(pagamento);
    }

    @Generated
    private MercadoPagoRequestDTO buildMercadoPagoRequestDTO(Pedido pedido) {
        log.info("Montando requisicao para enviar ao Mercado Pago");
        var pagamento = findPagamentoByPedidoId(pedido.getId());
        var produtos = pedido.getProdutos();

        List<ItemDTO> items = new ArrayList<>();
        log.info("Iterando pela lista de produtos para montar os itens");
        produtos.forEach((produto) -> {
            var count = produtoService.count(produto.getId(), produtos);
            items.add(ItemDTO.builder()
                    .category(produto.getCategoria().getDescricao())
                    .title(produto.getDescricao() + " do pedido " + pedido.getSenhaRetirada())
                    .description(produto.getDescricao())
                    .unitPrice(produto.getValorUnitario())
                    .quantity(count)
                    .unitMeasure("unit")
                    .totalAmount(produto.getValorUnitario().multiply(BigDecimal.valueOf(count)))
                    .build()
            );
        });

        log.info("Retornando MercadoPagoRequestDTO");
        return MercadoPagoRequestDTO.builder()
                .externalReference(pedido.getSenhaRetirada().toString())
                .title("Ordem de pedido")
                .description(String.format("Pagamento %d do Pedido %d", pagamento.getId(), pedido.getId()))
                .totalAmount(pagamento.getValorTotal().multiply(BigDecimal.valueOf(2L)))
                .items(items)
                .cashOut(CashOutDTO.builder().amount(pagamento.getValorTotal()).build())
                .build();
    }

    @Generated
    private Pedido getPedido(Long idPedido) {
        log.info("Buscando pedido por id {}", idPedido);
        return pedidoRepository.findById(idPedido).orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrado."));
    }

}
