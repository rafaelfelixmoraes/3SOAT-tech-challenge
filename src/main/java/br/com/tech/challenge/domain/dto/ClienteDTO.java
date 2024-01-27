package br.com.tech.challenge.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClienteDTO {

    private Long id;
    @NotBlank(message = "{cliente.nome.campo.obrigatorio}")
    @Size(min = 3, max = 200, message = "{cliente.nome.tamanho.invalido}")
    private String nome;
    @CPF(message = "{cliente.cpf.invalido}")
    private String cpf;
    @Email(message = "{cliente.email.invalido}")
    private String email;

}
