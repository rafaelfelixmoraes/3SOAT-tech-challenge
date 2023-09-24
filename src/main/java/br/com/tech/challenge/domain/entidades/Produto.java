package br.com.tech.challenge.domain.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Produto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Set<Pedido> pedidos;
}
