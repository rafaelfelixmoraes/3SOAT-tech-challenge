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
    private ProdutoService produtoSrvice;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ModelMapper mapper;

    @DisplayName("Deve criar um produto com sucesso")
    @Test
    void criarProdutoTest() {
        when(produtoRepository.save(any())).thenReturn(produto());

        Produto produto = produtoSrvice.salvar(mapper.map(produto(), ProdutoDTO.class));

        assertEquals(produto().getId(), produto.getId());
        assertEquals(produto().getDescricao(), produto.getDescricao());
        assertEquals(produto().getValorUnitario(), produto.getValorUnitario());
        assertEquals(produto().getCategoria(), produto.getCategoria());
        assertEquals(produto().getId().getClass(), produto.getId().getClass());
        assertEquals(produto().getDescricao().getClass(), produto.getDescricao().getClass());
        assertEquals(produto().getValorUnitario().getClass(), produto.getValorUnitario().getClass());
        assertEquals(produto().getCategoria().getClass(), produto.getCategoria().getClass());
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
