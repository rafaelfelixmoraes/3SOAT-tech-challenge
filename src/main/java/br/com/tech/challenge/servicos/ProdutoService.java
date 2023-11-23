package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.ProdutoRepository;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.ProdutoUpdateDTO;
import br.com.tech.challenge.domain.entidades.Produto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final ModelMapper mapper;

    @Transactional
    public Produto save(ProdutoDTO produtoDTO) {
        log.info("Salvando produto {}", produtoDTO);
        return produtoRepository.save(mapper.map(produtoDTO, Produto.class));
    }

    @Transactional
    public Produto update(final ProdutoUpdateDTO produtoUpdateDTO) {
        log.info("Atualizando produto {}", produtoUpdateDTO);
        var produto = produtoRepository.findById(produtoUpdateDTO.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Nenhum registro encontrado para o id informado"));

        mapper.map(produtoUpdateDTO, produto);

        return produtoRepository.save(produto);
    }

    public Page<Produto> list(Long id, Long categoria, int pagina, int tamanho) {
        log.info("Listando os produtos com os seguintes parametros id={}, categoria={}, pagina={}, tamanho={}",
                id, categoria, pagina, tamanho);
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("descricao"));

        if (ObjectUtils.anyNotNull(id, categoria)) {
            return produtoRepository.findByIdOrCategoriaId(id, categoria, pageable);
        }

        log.info("Retornando lista de produtos");
        return produtoRepository.findAll(pageable);
    }
    @Transactional
    public void delete(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ObjectNotFoundException("Nenhum registro encontrado para o ID informado");
        }

        log.info("Deletando o produto de id {}", id);
        produtoRepository.deleteById(id);
    }

    public long count(Long id, List<Produto> produtos) {
        log.info("Contando os produtos com o id {}", id);
        return produtos.stream().filter(produto -> produto.getId().equals(id)).count();
    }

    public Optional<Produto> findById(Long id) {
        log.info("Buscando por id {}", id);
        return produtoRepository.findById(id);
    }

}
