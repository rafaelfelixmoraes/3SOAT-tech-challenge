package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Pagamento;
import br.com.tech.challenge.domain.enums.StatusPedido;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PedidoDTO {

    private Long id;

    @Schema(description = "Senha para retirada do pedido")
    private Integer senhaRetirada;

    @Schema(description = "Cliente que realizou o pedido")
    private ClienteDTO cliente;

    @Schema(description = "Lista de produtos do pedido")
    private List<ProdutoDTO> produtos;

    @Schema(description = "Valor total do pedido")
    private BigDecimal valorTotal;

    @Schema(description = "Status do pedido")
    private StatusPedido statusPedido;

    @Schema(description = "Pagamento do pedido")
    @JsonIgnore
    private Pagamento pagamento;

    @Schema(description = "Data e hora do pedido")
    @JsonIgnore
    private LocalDateTime dataHora;

}
