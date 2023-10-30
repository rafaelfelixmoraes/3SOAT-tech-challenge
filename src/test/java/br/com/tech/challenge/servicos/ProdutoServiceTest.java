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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
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

    @DisplayName("Deve listar produtos com sucesso")
    @Test
    void shouldListProdutoSuccess() {
        var listProdutos = new PageImpl<>(setListProdutos());

        when(produtoRepository.findAll(any(Pageable.class))).thenReturn(listProdutos);

        var produtos = produtoService.list(null, null, anyInt(), 10);

        var listProdutosContent = produtos.getContent();

        for (int i = 0; i < listProdutosContent.size(); i++) {
            assertEquals(listProdutosContent.get(i).getId(), produtos.getContent().get(i).getId());
            assertEquals(listProdutosContent.get(i).getDescricao(), produtos.getContent().get(i).getDescricao());
            assertEquals(listProdutosContent.get(i).getValorUnitario(), produtos.getContent().get(i).getValorUnitario());
            assertEquals(listProdutosContent.get(i).getCategoria(), produtos.getContent().get(i).getCategoria());
            assertEquals(listProdutosContent.get(i).getId().getClass(), produtos.getContent().get(i).getId().getClass());
            assertEquals(listProdutosContent.get(i).getDescricao().getClass(), produtos.getContent().get(i).getDescricao().getClass());
            assertEquals(listProdutosContent.get(i).getValorUnitario().getClass(), produtos.getContent().get(i).getValorUnitario().getClass());
            assertEquals(listProdutosContent.get(i).getCategoria().getClass(), produtos.getContent().get(i).getCategoria().getClass());
        }
    }

    @DisplayName("Deve listar produto por id com sucesso")
    @Test
    void shouldListProdutoByIdSuccess() {
        var pageProdutos = new PageImpl<>(setListProdutos());

        when(produtoRepository.findByIdOrCategoriaId(anyLong(), anyLong(), any(Pageable.class))).thenReturn(pageProdutos);

        var produtoFound = produtoService.list(anyLong(), anyLong(), anyInt(), 10);

        int i = 0;
        assertEquals(pageProdutos.getContent().get(i).getId(), produtoFound.getContent().get(i).getId());
        assertEquals(pageProdutos.getContent().get(i).getDescricao(), produtoFound.getContent().get(i).getDescricao());
        assertEquals(pageProdutos.getContent().get(i).getValorUnitario(), produtoFound.getContent().get(i).getValorUnitario());
        assertEquals(pageProdutos.getContent().get(i).getCategoria(), produtoFound.getContent().get(i).getCategoria());
        assertEquals(pageProdutos.getContent().get(i).getId().getClass(), produtoFound.getContent().get(i).getId().getClass());
        assertEquals(pageProdutos.getContent().get(i).getDescricao().getClass(), produtoFound.getContent().get(i).getDescricao().getClass());
        assertEquals(pageProdutos.getContent().get(i).getValorUnitario().getClass(), produtoFound.getContent().get(i).getValorUnitario().getClass());
        assertEquals(pageProdutos.getContent().get(i).getCategoria().getClass(), produtoFound.getContent().get(i).getCategoria().getClass());
    }

    @DisplayName("Deve listar produtos vazios com sucesso")
    @Test
    void shouldListEmptyProdutoSuccess() {
        when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

        var produtos = produtoRepository.findAll();

        assertTrue(produtos.isEmpty());
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

    @DisplayName("Deve contar a quantidade de produtos por id na lista corretamente")
    @Test
    void shouldCountProdutosByIdCorrectly() {
        var produtos = setListProdutosWithOnlyId();

        assertEquals(2L, produtoService.count(1L, produtos));
        assertEquals(1L, produtoService.count(2L, produtos));
        assertEquals(1L, produtoService.count(3L, produtos));
    }

    @DisplayName("Deve encontrar o produto pelo id")
    @Test
    void shouldFindProdutoById() {
        final Long id = 1L;

        when(produtoService.findById(id)).thenReturn(Optional.of(setProduto()));

        assertTrue(produtoService.findById(id).isPresent());
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

    private List<Produto> setListProdutosWithOnlyId() {
        return List.of(
                Produto.builder().id(1L).build(),
                Produto.builder().id(1L).build(),
                Produto.builder().id(2L).build(),
                Produto.builder().id(3L).build()
        );
    }

    private List<Produto> setListProdutos() {
        var produto = Produto.builder()
                .id(1L)
                .descricao("Coca Cola")
                .valorUnitario(BigDecimal.valueOf(5.00))
                .categoria(setCategoria())
                .build();
        return Collections.singletonList(produto);
    }

}
