package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.bd.repositorios.ProdutoRepository;
import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.ProdutoUpdateDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ProdutoServiceTest {

    private final ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    ModelMapper mapper;

    ProdutoServiceTest() {
        MockitoAnnotations.openMocks(this);
        produtoService = new ProdutoService(produtoRepository, mapper);
    }

    @DisplayName("Deve criar um produto com sucesso")
    @Test
    void shouldCreateProdutoSuccess() {
        var returnedProduto = setProduto();
        var returnedProdutoDTO = setProdutoDTO();

        when(produtoRepository.save(any())).thenReturn(returnedProduto);

        var produto = produtoService.save(returnedProdutoDTO);

        assertEquals(returnedProduto.getId(), produto.getId());
        assertEquals(returnedProduto.getDescricao(), produto.getDescricao());
        assertEquals(returnedProduto.getValorUnitario(), produto.getValorUnitario());
        assertEquals(returnedProduto.getCategoria(), produto.getCategoria());
        assertEquals(returnedProduto.getId().getClass(), produto.getId().getClass());
        assertEquals(returnedProduto.getDescricao().getClass(), produto.getDescricao().getClass());
        assertEquals(returnedProduto.getValorUnitario().getClass(), produto.getValorUnitario().getClass());
        assertEquals(returnedProduto.getCategoria().getClass(), produto.getCategoria().getClass());
    }

    @DisplayName("Deve alterar um produto com sucesso")
    @Test
    void shouldUpdateProdutoSuccess(){
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

    @DisplayName("Deve deletar um produto com sucesso")
    @Test
    void shouldDeleteProdutoSuccess() {
        var produto = setProduto();

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(produto));

        produtoRepository.deleteById(anyLong());
    }

    @DisplayName("Deve retornar exceção ao tentar alterar um produto inexistente")
    @Test
    void shouldThrowWhenUpdateProdutoNotFound(){
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrowsExactly(ObjectNotFoundException.class, () ->
                produtoService.update(setProdutoUpdateDTO()), "Nenhum registro encontrado para o id informado");
    }

    @DisplayName("Deve retornar exceção ao tentar deletar um produto inexistente")
    @Test
    void shouldThrowWhenDeleteProdutoNotFound(){
        var produto = setProduto();

        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrowsExactly(ObjectNotFoundException.class, () ->
                produtoService.delete(produto.getId()), "Nenhum registro encontrado para o id informado");
    }

    private Produto setProduto() {
        return Produto.builder()
                .id(1L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(5.00))
                .categoria(setCategoria())
                .build();
    }

    private ProdutoDTO setProdutoDTO() {
        return ProdutoDTO.builder()
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
