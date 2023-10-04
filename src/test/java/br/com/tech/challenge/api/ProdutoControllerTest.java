package br.com.tech.challenge.api;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.domain.dto.ProdutoUpdateDTO;
import br.com.tech.challenge.domain.entidades.Categoria;
import br.com.tech.challenge.domain.entidades.Produto;
import br.com.tech.challenge.servicos.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper mapper;

    private static final String ROTA_PRODUTOS = "/produtos";

    @DisplayName("Deve salvar um produto com sucesso")
    @Test
    void saveProdutoSuccess() throws Exception {
        Mockito.when(produtoService.save(Mockito.any())).thenReturn(setProduto());

        mockMvc.perform(post(ROTA_PRODUTOS)
                        .content(mapper.writeValueAsString(setProduto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("Deve Alterar um produto com sucesso")
    @Test
    void updateProdutoSuccess() throws Exception {
        var produtoUpdateDTO = setProdutoUpdateDTO();

        var produtoEntity = Produto.builder()
                .id(produtoUpdateDTO.getId())
                .descricao(produtoUpdateDTO.getDescricao())
                .categoria(produtoUpdateDTO.getCategoria())
                .valorUnitario(produtoUpdateDTO.getValorUnitario())
                .build();

        Mockito.when(produtoService.update(Mockito.any())).thenReturn(produtoEntity);

        mockMvc.perform(patch(ROTA_PRODUTOS + "/1")
                        .content(mapper.writeValueAsString(produtoUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @DisplayName("Deve retornar excessão ao tentar alterar um produto inexistente")
    @Test
    void produtoUpdateNotFounded() throws Exception {
        Mockito.when(produtoService.update(Mockito.any())).thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(patch(ROTA_PRODUTOS + "/10")
                        .content(mapper.writeValueAsString(setProdutoUpdateDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()));
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
