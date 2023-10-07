package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Cliente;
import br.com.tech.challenge.domain.entidades.Pagamento;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.domain.enums.StatusPedido;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoDTO {

    private Long id;

    private Integer senhaRetirada;

    private Cliente cliente;

    private List<Produto> produtos;

    private BigDecimal valorTotal;

    private StatusPedido statusPedido;

    private Pagamento pagamento;

}
