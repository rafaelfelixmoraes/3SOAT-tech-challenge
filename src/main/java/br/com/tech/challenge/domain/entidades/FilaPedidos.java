package br.com.tech.challenge.domain.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Generated;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Generated
@Builder(toBuilder = true)
@Table(name = "View_FilaPedidos")
@AllArgsConstructor
@NoArgsConstructor
@Subselect("SELECT * FROM View_FilaPedidos ORDER BY data_hora DESC")
public class FilaPedidos {

    @Id
    @Column(name = "senha")
    private Integer senhaRetirada;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @Column(name = "status_pedido")
    private String statusPedido;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

}
