package br.com.tech.challenge.bd.repositorios;

import br.com.tech.challenge.domain.entidades.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

   Optional<Cliente> findByCpf(String cpf);
   Page<Cliente> findById(Long id, Pageable pageable);

}
