package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
