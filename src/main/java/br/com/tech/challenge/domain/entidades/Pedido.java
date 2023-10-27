package br.com.tech.challenge.domain.entidades;

import br.com.tech.challenge.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table
@Generated
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_sequence")
    @SequenceGenerator(name = "pedido_sequence", sequenceName = "pedido_seq", initialValue = 1, allocationSize = 1)
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
    @Column(name = "status_pedido")
    private StatusPedido statusPedido;

    @OneToOne(mappedBy = "pedido")
    @Transient
    private Pagamento pagamento;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

}
