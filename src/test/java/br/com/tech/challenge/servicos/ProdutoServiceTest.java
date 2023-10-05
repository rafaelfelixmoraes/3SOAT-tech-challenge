package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.ProdutoRepository;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.ProdutoUpdateDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyLong;
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
        when(produtoRepository.save(any())).thenReturn(setProduto());

        var produto = produtoService.save(mapper.map(setProduto(), ProdutoDTO.class));

        var returnedProduto = setProduto();

        assertEquals(returnedProduto.getId(), produto.getId());
        assertEquals(returnedProduto.getDescricao(), produto.getDescricao());
        assertEquals(returnedProduto.getValorUnitario(), produto.getValorUnitario());
        assertEquals(returnedProduto.getCategoria(), produto.getCategoria());
        assertEquals(returnedProduto.getId().getClass(), produto.getId().getClass());
        assertEquals(returnedProduto.getDescricao().getClass(), produto.getDescricao().getClass());
        assertEquals(returnedProduto.getValorUnitario().getClass(), produto.getValorUnitario().getClass());
        assertEquals(returnedProduto.getCategoria().getClass(), produto.getCategoria().getClass());
    }

    @DisplayName("Deve Alterar um produto com sucesso")
    @Test
    void updateProdutoSuccess(){
        var produtoUpdateDTO = setProdutoUpdateDTO();

        var produtoEntity = Produto.builder()
                .id(produtoUpdateDTO.getId())
                .descricao(produtoUpdateDTO.getDescricao())
                .categoria(produtoUpdateDTO.getCategoria())
                .valorUnitario(produtoUpdateDTO.getValorUnitario())
                .build();

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(setProduto()));
        when(produtoRepository.save(any())).thenReturn(produtoEntity);

        var produtoUpdated = produtoService.update(produtoUpdateDTO);

        assertEquals(produtoEntity.getId(), produtoUpdated.getId());
        assertEquals(produtoEntity.getDescricao(), produtoUpdated.getDescricao());
        assertEquals(produtoEntity.getValorUnitario(), produtoUpdated.getValorUnitario());
        assertEquals(produtoEntity.getCategoria(), produtoUpdated.getCategoria());
        assertEquals(produtoEntity.getId().getClass(), produtoUpdated.getId().getClass());
        assertEquals(produtoEntity.getDescricao().getClass(), produtoUpdated.getDescricao().getClass());
        assertEquals(produtoEntity.getValorUnitario().getClass(), produtoUpdated.getValorUnitario().getClass());
        assertEquals(produtoEntity.getCategoria().getClass(), produtoUpdated.getCategoria().getClass());
    }

    @DisplayName("Deve retornar excessão ao tentar alterar um produto inexistente")
    @Test
    void produtoUpdateNotFounded(){
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrowsExactly(ObjectNotFoundException.class, () ->
                produtoService.update(setProdutoUpdateDTO()), "Nenhum registro encontrado para o id informado");
    }

    private Produto setProduto() {
        return Produto.builder()
                .id(1L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(5.00))
                .categoria(setCategoria())
                .build();
    }

    private Categoria setCategoria() {
        return Categoria.builder()
                .id(2L)
                .descricao("Bebida")
                .build();
    }

    private ProdutoUpdateDTO setProdutoUpdateDTO() {
        return ProdutoUpdateDTO.builder()
                .id(1L)
                .descricao("Produto Alterado Teste")
                .categoria(setCategoria())
                .valorUnitario(new BigDecimal("10.50"))
                .build();
    }

}
