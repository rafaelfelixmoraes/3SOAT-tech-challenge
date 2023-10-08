package br.com.tech.challenge.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteCpfDTO {


    private Long id;

    @CPF(message = "CPF deve ser válido e no formato ###.###.###-##")
    private String cpf;


}
