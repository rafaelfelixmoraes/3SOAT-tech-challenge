package br.com.tech.challenge.api;


import br.com.tech.challenge.dto.RequestTransactionDto;
import br.com.tech.challenge.dto.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction")
public class TransactionController {


    @Operation(description = "API para criar uma transação financeira")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Recurso criado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "401", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> enviarTransacao(@RequestBody final RequestTransactionDto requestTransactionDto) {
        return Mono.empty();
    }

    @Operation(description = "API para buscar as transações persistidas por id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retorno Ok da lista de transações"),
            @ApiResponse(responseCode = "404", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "401", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> buscarTransacao(@PathVariable("id") final String uuid) {
        return Mono.empty();
    }

    @Operation(description = "API para deletar as transações persistidas por id")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Recurso excluido com sucesso."),
            @ApiResponse(responseCode = "404", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "401", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> removerTransacao(@PathVariable("id") final String uuid) {
        return Mono.empty();
    }


    @Operation(description = "API para autorizar a transação financeira")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Recurso editado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Erro de autenticação dessa API"),
            @ApiResponse(responseCode = "401", description = "Erro de autorização dessa API"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")})
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @PatchMapping(value = "/{id}/confimar")
    public Mono<TransactionDto> confirmarTransacao(@PathVariable("id") final String uuid) {
        return Mono.empty();
    }

}
