package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.ProdutoDTO;
import br.com.tech.challenge.servicos.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produto")
public class ProdutoConttoller {

    private final ProdutoService produtoService;

    private final ModelMapper mapper;

    @Operation(description = "API para criar uma Produto")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Produto criado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
            @ApiResponse(responseCode = "401", description = "Não autorizado.")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProdutoDTO> salvar(@RequestBody ProdutoDTO produtoDTO) {
        return ResponseEntity.ok().body(mapper.map(produtoService.salvar(produtoDTO), ProdutoDTO.class));
    }

}
