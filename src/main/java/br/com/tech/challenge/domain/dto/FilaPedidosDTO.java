package br.com.tech.challenge.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FilaPedidosDTO {

    @Schema(description = "Senha para retirada do pedido")
    private Integer senhaRetirada;

    @Schema(description = "Nome do Cliente")
    private String nomeCliente;

    @Schema(description = "Status atual do pedido")
    private String statusPedido;
}
