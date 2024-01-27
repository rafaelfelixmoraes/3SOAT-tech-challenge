package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Categoria;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class ProdutoUpdateDTO {

    @JsonIgnore
    private Long id;

    @Size(min = 3, max = 100, message = "{produto.descricao.tamanho.invalido}")
    @NotBlank(message = "{produto.descricao.campo.obrigatorio}")
    @Schema(description = "Descrição do Produto")
    private String descricao;

    @Schema(description = "Categoria do Produto")
    private Categoria categoria;

    @PositiveOrZero(message = "{produto.valor.unitario.positivo}")
    @Schema(description = "Valor unitário do produto")
    private BigDecimal valorUnitario;

}
