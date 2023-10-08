package br.com.tech.challenge.domain.entidades;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.Immutable;

@Data
@Immutable
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FilaPedidos {

    @Column(name = "senha")
    private Integer senhaRetirada;

    @Column(name = "nomecliente")
    private String nomeCliente;

    @Column(name = "statuspedido")
    private String statusPedido;

}
