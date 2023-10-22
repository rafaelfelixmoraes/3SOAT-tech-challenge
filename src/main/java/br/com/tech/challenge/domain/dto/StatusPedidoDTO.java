package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusPedidoDTO {

    @NotBlank
    StatusPedido statusPedido;

}
