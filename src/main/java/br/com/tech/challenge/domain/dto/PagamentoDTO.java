package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.enums.StatusPagamento;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PagamentoDTO {

    private Long id;

    @Schema(description = "Pedido vinculado ao pagamento")
    private PedidoDTO pedido;

    @Schema(description = "Data e hora de quando o pagamento é realizado")
    private LocalDateTime dataHoraPagamento;

    @Schema(description = "Soma do valor de todos os produtos do pedido")
    private BigDecimal valorTotal;

    @Schema(description = "Atributo para armazenar hash do pagamento")
    private String qrData;

    @Schema(description = "Status do pagamento")
    private StatusPagamento statusPagamento;

}
