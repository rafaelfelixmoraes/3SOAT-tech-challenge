package br.com.tech.challenge.domain.entidades;

import br.com.tech.challenge.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table
@Generated
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagamento_sequence")
    @SequenceGenerator(name = "pagamento_sequence", sequenceName = "pagamento_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id", referencedColumnName = "id")
    private Pedido pedido;

    @Column(name = "data_hora_pagamento")
    private LocalDateTime dataHoraPagamento;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "qr_data")
    private String qrData;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento")
    private StatusPagamento statusPagamento;

}
