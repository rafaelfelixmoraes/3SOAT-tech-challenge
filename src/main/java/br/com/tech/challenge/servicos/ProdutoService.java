package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.ProdutoRepository;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.ProdutoUpdateDTO;
import br.com.tech.challenge.domain.entidades.Produto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
