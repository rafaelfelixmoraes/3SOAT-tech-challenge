package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.servicos.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Endpoints para controle de Clientes")
public class ClienteController {

    private final ClienteService clienteService;

    private final ModelMapper mapper;

    @Operation(description = "Endpoint para criar um Cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Cliente inválido."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
        }
    )
    @PostMapping
    public ResponseEntity<ClienteDTO> save(@RequestBody @Valid ClienteDTO clienteDTO) {
        return ResponseEntity.status(CREATED).body(mapper.map(clienteService.save(clienteDTO), ClienteDTO.class));
    }

}
