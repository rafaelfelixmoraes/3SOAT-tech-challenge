package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.ProdutoRepository;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ModelMapper mapper;

    @DisplayName("Deve criar um produto com sucesso")
    @Test
    void createProdutoSuccess() {
        when(produtoRepository.save(any())).thenReturn(produto());

        var produto = produtoService.save(mapper.map(produto(), ProdutoDTO.class));

        var returnedProduto = produto();

        assertEquals(returnedProduto.getId(), produto.getId());
        assertEquals(returnedProduto.getDescricao(), produto.getDescricao());
        assertEquals(returnedProduto.getValorUnitario(), produto.getValorUnitario());
        assertEquals(returnedProduto.getCategoria(), produto.getCategoria());
        assertEquals(returnedProduto.getId().getClass(), produto.getId().getClass());
        assertEquals(returnedProduto.getDescricao().getClass(), produto.getDescricao().getClass());
        assertEquals(returnedProduto.getValorUnitario().getClass(), produto.getValorUnitario().getClass());
        assertEquals(returnedProduto.getCategoria().getClass(), produto.getCategoria().getClass());
    }

    private Produto produto() {
        return Produto.builder()
                .id(1L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(5.00))
                .categoria(categoria())
                .build();
    }

    private Categoria categoria() {
        return Categoria.builder()
                .id(2L)
                .descricao("Bebida")
                .build();
    }

}
