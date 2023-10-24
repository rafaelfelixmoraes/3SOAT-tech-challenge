package br.com.tech.challenge.api.client;

import br.com.tech.challenge.domain.dto.MercadoPagoRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MercadoPagoClient {

    private final String URL_MERCADO_PAGO = "https://api.mercadopago.com";
    private final String ACCESS_TOKEN = "Bearer TEST-6621802098599609-102118-84e7d61f11ef646bbaf89a78e1bb2631-700064145";
    private final WebClient client;

    // TODO: criar método para criar um caixa (QR Code)

    public MercadoPagoClient() {
        client = WebClient.create(URL_MERCADO_PAGO);
    }

    public void createCaixa(MercadoPagoRequestDTO dto) {
        Mono<ResponseEntity<Person>> result = client.post()
                .uri("/pos")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", ACCESS_TOKEN)
                .bodyValue(dto)
                .retrieve()
                .bodyToEntity(Void.class);
    }

    // TODO: criar um método para obter um caixa (QR Code)
    public void retrieveCaixa(Integer id) {

    }

}
