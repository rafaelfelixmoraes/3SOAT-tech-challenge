package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.client.MercadoPagoClient;
import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.PagamentoRepository;
import br.com.tech.challenge.bd.repositorios.PedidoRepository;
import br.com.tech.challenge.domain.dto.external.CashOutDTO;
import br.com.tech.challenge.domain.dto.external.ItemDTO;
import br.com.tech.challenge.domain.dto.external.MercadoPagoRequestDTO;
import br.com.tech.challenge.domain.dto.external.MercadoPagoResponseDTO;
import br.com.tech.challenge.domain.dto.external.SponsorDTO;
import br.com.tech.challenge.domain.entidades.Pagamento;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.enums.MercadoPagoAPI;
import br.com.tech.challenge.domain.enums.StatusPagamento;
import br.com.tech.challenge.domain.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    private final MercadoPagoClient mercadoPagoClient;

    private final PedidoRepository pedidoRepository;

    private final ProdutoService produtoService;

    @Transactional
    public Pagamento save(Pedido pedido, BigDecimal valorTotal) {
        var pagamento = Pagamento.builder()
                .pedido(pedido)
                .dataHoraPagamento(LocalDateTime.now())
                .valorTotal(valorTotal)
                .statusPagamento(StatusPagamento.AGUARDANDO_PAGAMENTO)
                .build();
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento findPagamentoByPedidoId(Long idPedido) {
        return pagamentoRepository.findPagamentoByPedidoId(idPedido).orElseThrow(() -> new ObjectNotFoundException("Pagamento não encontrado."));
    }

    @Transactional
    public MercadoPagoResponseDTO generateQRCode(Long idPedido) {
        var pedido = getPedido(idPedido);
        var requestDTO = buildMercadoPagoRequestDTO(pedido);
        var responseDTO = mercadoPagoClient.generateQRCode(requestDTO);

        var pagamento = pedido.getPagamento();
        pagamento.setQrData(responseDTO.getQrData());

        pagamentoRepository.save(pagamento);
        return responseDTO;
    }

    @Transactional
    public void payQRCode(Long idPedido) {
        var pedido = getPedido(idPedido);
        var requestDTO = buildMercadoPagoRequestDTO(pedido);
        mercadoPagoClient.payQRCode(requestDTO);

        pedido.setStatusPedido(StatusPedido.EM_PREPARACAO);
        pedidoRepository.save(pedido);
    }

    private MercadoPagoRequestDTO buildMercadoPagoRequestDTO(Pedido pedido) {
        var pagamento = findPagamentoByPedidoId(pedido.getId());
        var produtos = pedido.getProdutos();

        List<ItemDTO> items = new ArrayList<>();
        produtos.forEach((produto) -> {
            items.add(ItemDTO.builder()
                    .category(produto.getCategoria().getDescricao())
                    .description(produto.getDescricao())
                    .unitPrice(produto.getValorUnitario())
                    .quantity(produtoService.count(produto.getId(), produtos))
                    .build()
            );
        });

        return MercadoPagoRequestDTO.builder()
                .externalReference(pedido.getSenhaRetirada().toString())
                .title(String.format("Pagamento %d do Pedido %d", pagamento.getId(), pedido.getId()))
                .totalAmount(pagamento.getValorTotal())
                .items(items)
                .sponsor(SponsorDTO.builder()
                        .id(Long.valueOf(MercadoPagoAPI.SPONSOR_ID.text()))
                        .build())
                .cashOut(CashOutDTO.builder()
                        .amount(pagamento.getValorTotal())
                        .build())
                .build();
    }

    private Pedido getPedido(Long idPedido) {
        return pedidoRepository.findById(idPedido).orElseThrow(() -> new ObjectNotFoundException("Pedido não encontrado."));
    }

}
