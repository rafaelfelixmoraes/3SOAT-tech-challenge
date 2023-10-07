package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.domain.dto.ProdutoUpdateDTO;
import br.com.tech.challenge.servicos.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Endpoints para controle de Produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    private final ModelMapper mapper;

    @Operation(description = "Endpoint para criar um Produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Produto inválido."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @PostMapping
    public ResponseEntity<ProdutoDTO> save(@RequestBody @Valid ProdutoDTO produtoDTO) {
        return ResponseEntity.status(CREATED).body(mapper.map(produtoService.save(produtoDTO), ProdutoDTO.class));
    }

    @Operation(summary = "Altera um Produto", description = "Endpoint para alterar um Produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto alterado com sucesso.",
                    content = {
                            @Content(mediaType = "application/json", schema =
                            @Schema(implementation = ProdutoDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @Parameters(value = {
            @Parameter(
                    name = "id",
                    description = "Identificador do Produto",
                    in = ParameterIn.PATH
            )
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProdutoDTO> update(@PathVariable("id") final Long id,
                                             @RequestBody @Valid ProdutoUpdateDTO produtoUpdateDTO) {
        produtoUpdateDTO.setId(id);
        return ResponseEntity.ok().body(
                mapper.map(
                        produtoService.update(produtoUpdateDTO),
                        ProdutoDTO.class
                )
        );
    }

}
