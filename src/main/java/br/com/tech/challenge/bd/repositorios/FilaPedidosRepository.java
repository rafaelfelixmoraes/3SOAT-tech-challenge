package br.com.tech.challenge.bd.repositorios;

import br.com.tech.challenge.domain.entidades.FilaPedidos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilaPedidosRepository extends JpaRepository<FilaPedidos, Long> {
}
