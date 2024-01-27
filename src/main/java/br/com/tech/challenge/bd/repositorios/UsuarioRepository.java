package br.com.tech.challenge.bd.repositorios;

import br.com.tech.challenge.domain.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String nomeUsuario);

    boolean existsByUsuario(String usuario);

}
