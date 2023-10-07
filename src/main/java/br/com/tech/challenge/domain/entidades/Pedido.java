package br.com.tech.challenge.domain.entidades;

import br.com.tech.challenge.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Integer senhaRetirada;

    @ManyToOne
    @JoinColumn(name = "cliente_id",  nullable=false)
    private Cliente cliente;

    @ManyToMany
    private List<Produto> produtos;

    @Column
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name="statusPedido")
    private StatusPedido statusPedido;

    @OneToOne(mappedBy = "pedido")
    @Transient
    private Pagamento pagamento;

}
