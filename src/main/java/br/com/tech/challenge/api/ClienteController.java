package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.servicos.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    private final ModelMapper mapper;

    @Operation(description = "Endpoint para criar um Cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Cliente inválido."),
            @ApiResponse(responseCode = "401", description = "Não autorizado.")
        }
    )
    @PostMapping
    public ResponseEntity<ClienteDTO> salvar(@RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok().body(mapper.map(clienteService.salvar(clienteDTO), ClienteDTO.class));
    }

}
