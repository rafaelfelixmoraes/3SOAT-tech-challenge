package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.ClienteCheckInDTO;
import br.com.tech.challenge.domain.dto.ClienteCpfDTO;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.dto.RequestClienteCpfDTO;
import br.com.tech.challenge.servicos.ClienteService;
import br.com.tech.challenge.utils.CpfUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
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
            @ApiResponse(responseCode = "409", description = "Cliente já cadastrado."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @PostMapping
    public ResponseEntity<ClienteDTO> save(@RequestBody @Valid ClienteDTO clienteDTO) {
        return ResponseEntity.status(CREATED).body(mapper.map(clienteService.save(clienteDTO), ClienteDTO.class));
    }


    @Operation(description = "Endpoint para criar um Cliente somente com cpf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Cliente inválido."),
            @ApiResponse(responseCode = "409", description = "Cliente já cadastrado."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @PostMapping("/cpf")
    public ResponseEntity<ClienteCpfDTO> saveCpf(@RequestBody RequestClienteCpfDTO clienteCpfDTO) {

        String formattedCPF = CpfUtils.formatCpf(clienteCpfDTO.getCpf());
        return ResponseEntity.status(CREATED).body(mapper.map(clienteService.saveClientWithCpf(formattedCPF), ClienteCpfDTO.class));
    }
    @Operation(description = "Endpoint para identificar cliente via cpf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente identificado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @PostMapping("/check-in")
    public ResponseEntity<ClienteCheckInDTO> checkInCliente(@RequestBody @Valid RequestClienteCpfDTO clienteCpfDTO) {

        String formattedCPF = CpfUtils.formatCpf(clienteCpfDTO.getCpf());
        return ResponseEntity.ok(clienteService.checkInCliente(CpfUtils.formatCpf(formattedCPF)));
    }

    @Operation(summary = "Lista os Clientes", description = "Endpoint para listar os clientes")
    @GetMapping
    public ResponseEntity<Page<ClienteDTO>> list(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            @RequestParam(name = "tamanho", defaultValue = "10") int tamanho
    ) {
        Page<ClienteDTO> clientes = clienteService.list(id, pagina, tamanho)
                .map(cliente -> mapper.map(cliente, ClienteDTO.class));
        return ResponseEntity.ok().body(clientes);
    }

}
