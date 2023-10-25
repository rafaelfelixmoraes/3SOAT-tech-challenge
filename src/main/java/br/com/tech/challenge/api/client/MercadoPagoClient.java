package br.com.tech.challenge.api.client;

import br.com.tech.challenge.api.client.factory.ClientHttpFactory;
import br.com.tech.challenge.api.client.factory.RetryTemplateFactory;
import br.com.tech.challenge.domain.dto.MercadoPagoRequestDTO;
import br.com.tech.challenge.domain.dto.MercadoPagoResponseDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class MercadoPagoClient implements QRClient {

    private final String ACCESS_TOKEN = "TEST-6621802098599609-102118-84e7d61f11ef646bbaf89a78e1bb2631-700064145";

    private final String MERCADO_PAGO_URL = "https://api.mercadopago.com";

    private final String USER_ID = "700064145";

    private final String CAIXA_PAGAMENTO_ID = "87956324";

    @Override
    public MercadoPagoResponseDTO generateQRCode(MercadoPagoRequestDTO dto) {
        RetryTemplate retryTemplate = RetryTemplateFactory.retryTemplate();
        RestTemplate restTemplate = new RestTemplate(ClientHttpFactory.clientHttpRequestFactory());

        String qrCodeUrl = String.format("/instore/orders/qr/seller/collectors/%s/pos/%s/qrs", USER_ID, CAIXA_PAGAMENTO_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", ACCESS_TOKEN);

        HttpEntity<MercadoPagoRequestDTO> httpEntity = new HttpEntity<>(dto, headers);

        ResponseEntity<MercadoPagoResponseDTO> responseEntity = retryTemplate
                .execute(retryContext -> restTemplate.postForEntity(
                        MERCADO_PAGO_URL.concat(qrCodeUrl), httpEntity, MercadoPagoResponseDTO.class)
                );

        return responseEntity.getBody();
    }

    @Override
    public boolean payQRCode(MercadoPagoRequestDTO dto) {
        RetryTemplate retryTemplate = RetryTemplateFactory.retryTemplate();
        RestTemplate restTemplate = new RestTemplate(ClientHttpFactory.clientHttpRequestFactory());

        String qrCodeUrl = String.format("/instore/orders/qr/seller/collectors/%s/pos/%s/qrs", USER_ID, CAIXA_PAGAMENTO_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", ACCESS_TOKEN);

        HttpEntity<MercadoPagoRequestDTO> httpEntity = new HttpEntity<>(dto, headers);

        try {
            retryTemplate.execute(retryContext -> restTemplate.exchange(
                    MERCADO_PAGO_URL.concat(qrCodeUrl),
                    HttpMethod.PUT,
                    httpEntity,
                    Void.class
            ));
            return true;
        } catch (RestClientException exception) {
            return false;
        }

    }

}
