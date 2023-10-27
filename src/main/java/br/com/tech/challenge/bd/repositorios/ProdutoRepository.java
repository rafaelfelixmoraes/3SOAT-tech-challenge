package br.com.tech.challenge.bd.repositorios;

import br.com.tech.challenge.domain.entidades.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByIdOrCategoriaId(Long id, Long categoriaId, Pageable pageable);
}
