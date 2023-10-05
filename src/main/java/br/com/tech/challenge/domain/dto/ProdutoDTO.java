package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Categoria;
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
public class ProdutoDTO {

    private Long id;

    @Size(min = 3, max = 100, message = "Descrição deve ter entre 3 e 100 caracteres")
    private String descricao;

    private Categoria categoria;

    @PositiveOrZero(message = "Valor unitário deve ser zero ou positivo")
    private BigDecimal valorUnitario;

}
