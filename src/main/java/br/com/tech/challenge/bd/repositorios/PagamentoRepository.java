package br.com.tech.challenge.bd.repositorios;

import br.com.tech.challenge.domain.entidades.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
