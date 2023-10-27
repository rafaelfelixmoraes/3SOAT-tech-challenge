package br.com.tech.challenge.domain.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table
@Generated
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_sequence")
    @SequenceGenerator(name = "produto_sequence", sequenceName = "produto_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(length = 100)
    private String descricao;

    @ManyToOne
    @JoinColumn(name="categoria_id", nullable=false)
    private Categoria categoria;

    @Column
    private BigDecimal valorUnitario;

    @ManyToMany
    @Transient
    private List<Pedido> pedidos;

}
