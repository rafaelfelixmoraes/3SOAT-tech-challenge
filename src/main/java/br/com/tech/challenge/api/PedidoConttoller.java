package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.servicos.PedidoService;
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
@RequestMapping("/pedidos")
public class PedidoConttoller {

    private final PedidoService pedidoService;

    private final ModelMapper mapper;

    @Operation(description = "API para criar um Pedido")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Pedido criado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado."),
            @ApiResponse(responseCode = "401", description = "Não autorizado.")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PedidoDTO> salvar(@RequestBody PedidoDTO pedidoDTO) {
        return ResponseEntity.ok().body(mapper.map(pedidoService.save(pedidoDTO), PedidoDTO.class));
    }

}
