package br.com.tech.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class BeneficiationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4024435519155849092L;


    @Schema(description = "CPF do Beneficiário.")
    @NotNull(message = "Informar o CPF.")
    private Long CPF;

    @Schema(description = "codigo do banco de destino.")
    @NotNull(message = "Informar o codigo do banco.")
    private Long codigoBanco;

    @Schema(description = "Agencia de destino.")
    @NotNull(message = "Informar a agencia de destino.")
    private String agencia;

    @Schema(description = "Conta de destino.")
    @NotNull(message = "Informar a conta de destino.")
    private String conta;

    @Schema(description = "Nome do Favorecido")
    @NotNull(message = "Informar o nome do Faavorecido")
    private String nomeFavorecido;
}
