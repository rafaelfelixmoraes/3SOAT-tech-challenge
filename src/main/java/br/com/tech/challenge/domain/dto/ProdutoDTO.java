package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Categoria;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
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

    @Size(min = 3, max = 100, message = "{produto.descricao.tamanho.invalido}")
    @NotBlank(message = "{produto.descricao.campo.obrigatorio}")
    private String descricao;

    private Categoria categoria;

    @PositiveOrZero(message = "{produto.valor.unitario.positivo}")
    @NotNull(message = "{produto.valor.unitario.nao.nulo}")
    private BigDecimal valorUnitario;

}
