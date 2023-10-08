package br.com.tech.challenge.api;

import br.com.tech.challenge.api.exception.ClienteAlreadyExistsException;
import br.com.tech.challenge.api.exception.InvalidCpfException;
import br.com.tech.challenge.domain.dto.ClienteCpfDTO;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.entidades.Cliente;
import br.com.tech.challenge.servicos.ClienteService;
import br.com.tech.challenge.utils.CpfUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clientes")
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
        this.isClienteAlreadyExists(clienteDTO.getCpf());
        return ResponseEntity.status(CREATED).body(mapper.map(clienteService.save(clienteDTO), ClienteDTO.class));
    }


    @Operation(description = "Endpoint para criar um Cliente somente com cpf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Cliente inválido."),
            @ApiResponse(responseCode = "409", description = "Cliente já cadastrado."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @PatchMapping("/{cpf}")
    public ResponseEntity<ClienteCpfDTO> saveCpf(@PathVariable String cpf) {

        String formattedCpf = CpfUtils.formatCpf(cpf);

        if (!CpfUtils.isCpfValid(formattedCpf)) {
            throw new InvalidCpfException("CPF inválido: " + formattedCpf);
        }

        this.isClienteAlreadyExists(formattedCpf);

        return ResponseEntity.status(CREATED).body(mapper.map(clienteService.saveClientWithCpf(formattedCpf), ClienteCpfDTO.class));
    }

    private void isClienteAlreadyExists(String cpf) {
        Cliente clienteAlreadyExists = clienteService.findByCpf(cpf);
        if (clienteAlreadyExists != null) {
            throw new ClienteAlreadyExistsException("Cliente já cadastrado");
        }
    }

}
