package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Pedido;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoDTO {

    private Long id;

    private String descricao;

    private Categoria categoria;

    private BigDecimal valorUnitario;

    private Set<Pedido> pedidos;

}
