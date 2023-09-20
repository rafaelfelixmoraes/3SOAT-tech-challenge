package br.com.tech.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@ToString
public class Conta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1080701735968423552L;

    @NotNull(message = "Informar o código da Agência")
    private Long codigoAgencia;

    @Schema(description = "Código da Conta")
    @NotNull(message = "Informar o código da Conta.")
    private Long codigoConta;
}
