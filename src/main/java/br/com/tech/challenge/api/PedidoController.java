package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.FilaPedidosDTO;
import br.com.tech.challenge.domain.dto.PedidoDTO;
import br.com.tech.challenge.servicos.FilaPedidosService;
import br.com.tech.challenge.servicos.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para controle de Pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    private final FilaPedidosService filaPedidosService;

    private final ModelMapper mapper;

    @Operation(description = "Endpoint para criar um Cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Pedido inválido."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PedidoDTO> salvar(@RequestBody PedidoDTO pedidoDTO) {
        return ResponseEntity.ok().body(mapper.map(pedidoService.save(pedidoDTO), PedidoDTO.class));
    }

    @Operation(summary = "Lista a fila de pedidos", description = "Endpoint para listagem da fila de pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fila de pedidos retornada com sucesso.",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = FilaPedidosDTO.class)))
                    }),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @GetMapping(value = "/fila", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FilaPedidosDTO>> listaFilaPedidos() {
        return ResponseEntity.ok()
                .body(mapper.map(
                        filaPedidosService.listaFilaPedidos(),
                        new TypeToken<List<FilaPedidosDTO>>() {}.getType())
                );
    }

}
