package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.PagamentoDTO;
import br.com.tech.challenge.domain.dto.external.MercadoPagoResponseDTO;
import br.com.tech.challenge.servicos.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Endpoints para efetuar Pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    private final ModelMapper mapper;

    @Operation(description = "Endpoint para gerar um código QR (futuramente)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Código QR criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Informações de pagamento inválidas."),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @PostMapping("/pedido/{idPedido}/qr")
    public ResponseEntity<MercadoPagoResponseDTO> generateQRCode(@PathVariable("idPedido") Long id) {
        return ResponseEntity.ok().body(pagamentoService.generateQRCode(id));
    }

    @Operation(description = "Endpoint para realizar o checkout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @PostMapping("/pedido/{idPedido}/checkout")
    public ResponseEntity<PagamentoDTO> checkout(@PathVariable("idPedido") Long id) {
        var pagamento = pagamentoService.checkout(id);
        return ResponseEntity.ok().body(mapper.map(pagamento, PagamentoDTO.class));
    }

}
