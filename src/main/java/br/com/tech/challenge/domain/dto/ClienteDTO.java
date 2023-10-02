package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Pedido;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteDTO {

    private Long id;
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nome;
    @CPF(message = "CPF deve ser válido e no formato ###.###.###-##")
    private String cpf;
    @Email(message = "E-mail deve ser válido e no formato username@email.com")
    private String email;

    private Pedido pedido;

}
