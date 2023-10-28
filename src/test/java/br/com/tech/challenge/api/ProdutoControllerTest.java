package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.ProdutoUpdateDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.servicos.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Mock
    private ProdutoService produtoService;

    private static final String ROTA_PRODUTOS = "/produtos";

    @DisplayName("Deve salvar um produto com sucesso")
    @Test
    void shouldSaveProdutoSuccess() throws Exception {
        mockMvc.perform(post(ROTA_PRODUTOS)
                        .content(mapper.writeValueAsString(setProduto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("Deve alterar um produto com sucesso")
    @Test
    void shouldUpdateProdutoSuccess() throws Exception {
        var produtoUpdateDTO = setProdutoUpdateDTO();

        mockMvc.perform(patch(ROTA_PRODUTOS + "/10")
                        .content(mapper.writeValueAsString(produtoUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Deve retornar exceção ao tentar alterar um produto inexistente")
    @Test
    void shouldThrowExceptionWhenProdutoDoesntExist() throws Exception {
        mockMvc.perform(patch(ROTA_PRODUTOS + "/100")
                        .content(mapper.writeValueAsString(setProdutoUpdateDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()));
    }

    @DisplayName("Deve listar os produtos com sucesso")
    @Test
    void shouldListProdutoSuccess() throws Exception {
        mockMvc.perform(get(ROTA_PRODUTOS)
                        .content(mapper.writeValueAsString(setListProdutos()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @DisplayName("Deve listar os produtos vazios com sucesso")
    @Test
    void shouldListEmptyProdutoSuccess() throws Exception {
        final var queryParam = String.valueOf(100L);

        when(produtoService.list(anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(Page.empty());

        mockMvc.perform(get(ROTA_PRODUTOS)
                        .param("id", queryParam)
                        .param("categoria", queryParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @DisplayName("Deve deletar um produto com sucesso")
    @Test
    void shouldDeleteProdutoSuccess() throws Exception {
        mockMvc.perform(delete(ROTA_PRODUTOS + "/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
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
                .id(10L)
                .descricao("Produto Alterado Teste")
                .categoria(setCategoria())
                .valorUnitario(new BigDecimal("10.50"))
                .build();
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
