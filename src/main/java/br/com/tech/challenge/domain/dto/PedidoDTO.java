package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Cliente;
import br.com.tech.challenge.domain.entidades.Pagamento;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoDTO {

    private Long id;

    @Schema(description = "Senha para retirada do pedido")
    private Integer senhaRetirada;

    @Schema(description = "Cliente que realizou o pedido")
    private Cliente cliente;

    @Schema(description = "Lista de produtos do pedido")
    private List<Produto> produtos;

    @Schema(description = "Valor total do pedido")
    private BigDecimal valorTotal;

    @Schema(description = "Status do pedido")
    private StatusPedido statusPedido;

    @Schema(description = "Pagamento do pedido")
    private Pagamento pagamento;

}
