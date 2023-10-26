package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.PagamentoRepository;
import br.com.tech.challenge.domain.entidades.Pagamento;
import br.com.tech.challenge.domain.entidades.Pedido;
import br.com.tech.challenge.domain.enums.StatusPagamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

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

}
