package br.com.tech.challenge.domain.entidades;

import br.com.tech.challenge.domain.enums.StatusPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.NoArgsConstructor;

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
    @SequenceGenerator(name = "pedido_sequence", sequenceName = "pedido_seq", allocationSize = 1)
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
