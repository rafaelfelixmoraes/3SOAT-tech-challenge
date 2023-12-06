package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.enums.Role;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotEmpty(message = "O campo usuario é obrigatório.")
    private String usuario;

    @NotEmpty(message = "O campo senha é obrigatório.")
    private String senha;

    @NotNull(message = "O campo role é obrigatório.")
    private Role role;

}
