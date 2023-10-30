package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Categoria;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProdutoDTO {

    private Long id;

    @Size(min = 3, max = 100, message = "Descrição deve ter entre 3 e 100 caracteres")
    @NotNull(message = "Descrição deve ser diferente de nulo")
    private String descricao;

    private Categoria categoria;

    @PositiveOrZero(message = "Valor unitário deve ser zero ou positivo")
    @NotNull(message = "Valor unitário deve ser diferente de nulo")
    private BigDecimal valorUnitario;

}
