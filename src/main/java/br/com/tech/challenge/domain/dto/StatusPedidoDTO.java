package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusPedidoDTO {

    @NotBlank(message = "{pedido.status.campo.obrigatorio}")
    StatusPedido statusPedido;

}
