package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Pedido;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDTO {

    private Long id;

    private String nome;

    private String cpf;

    private String email;

    private Pedido pedido;

}
