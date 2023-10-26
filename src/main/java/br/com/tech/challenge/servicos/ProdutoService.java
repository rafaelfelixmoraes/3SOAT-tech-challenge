package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.ProdutoRepository;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.ProdutoUpdateDTO;
import br.com.tech.challenge.domain.entidades.Produto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final ModelMapper mapper;

    @Transactional
    public Produto save(ProdutoDTO produtoDTO) {
        return produtoRepository.save(mapper.map(produtoDTO, Produto.class));
    }

    @Transactional
    public Produto update(final ProdutoUpdateDTO produtoUpdateDTO) {
        var produto = produtoRepository.findById(produtoUpdateDTO.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Nenhum registro encontrado para o id informado"));

        mapper.map(produtoUpdateDTO, produto);

        return produtoRepository.save(produto);
    }

    public Page<Produto> list(Long id, Long categoria, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("descricao"));

        if (id != null) {
            Produto produto = produtoRepository.findById(id)
                    .orElse(null);
            if (produto != null) {
                return new PageImpl<>(Collections.singletonList(produto), pageable, 1);
            } else {
                return Page.empty(pageable);
            }
        } else if (categoria != null) {
            return produtoRepository.findByCategoriaId(categoria, pageable);
        } else {
            return produtoRepository.findAll(pageable);
        }
    }
    @Transactional
    public void delete(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ObjectNotFoundException("Nenhum registro encontrado para o ID informado");
        }

        produtoRepository.deleteById(id);
    }

}
