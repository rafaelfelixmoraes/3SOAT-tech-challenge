package br.com.tech.challenge.domain.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 200)
    private String nome;

    @Column
    private String cpf;

    @Column
    private String email;

    @OneToOne(mappedBy = "cliente")
    @Transient
    private Pedido pedido;
}
