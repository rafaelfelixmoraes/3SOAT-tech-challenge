package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.enums.Role;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@Generated
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "{usuario.usuario.campo.obrigatorio}")
    @Size(min = 2, max = 255, message = "{usuario.usuario.tamanho.invalido}")
    private String usuario;

    @NotBlank(message = "{usuario.senha.campo.obrigatorio}")
    @Size(min = 3, max = 255, message = "{usuario.usuario.tamanho.invalido}")
    private String senha;

    @NotNull(message = "{usuario.role.campo.obrigatorio}")
    private Role role;

}
