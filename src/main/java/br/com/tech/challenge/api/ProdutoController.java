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

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    private final ModelMapper mapper;

    @Operation(description = "Endpoint para criar um Produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Produto inválido."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
        }
    )
    @PostMapping
    public ResponseEntity<ProdutoDTO> salvar(@RequestBody @Valid ProdutoDTO produtoDTO) {
        return ResponseEntity.ok().body(mapper.map(produtoService.salvar(produtoDTO), ProdutoDTO.class));
    }

}
