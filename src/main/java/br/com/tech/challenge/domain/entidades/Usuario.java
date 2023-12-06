package br.com.tech.challenge.domain.entidades;

import br.com.tech.challenge.domain.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@Generated
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_sequence")
    @SequenceGenerator(name = "usuario_sequence", sequenceName = "usuario_seq", allocationSize = 1)
    private Long id;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "senha")
    private String senha;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

}
