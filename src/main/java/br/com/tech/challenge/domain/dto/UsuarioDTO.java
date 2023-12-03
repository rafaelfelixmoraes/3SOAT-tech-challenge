package br.com.tech.challenge.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Generated
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsuarioDTO {

    private Long id;

    @Size(min = 2, max = 255, message = "{usuario.nomeusario.tamanho.invalido}")
    private String nomeUsuario;

    @Size(min = 8, max = 255, message = "{usuario.senha.tamanho.invalido}")
    private String senha;

    private String role;

}
