package br.com.tech.challenge.api.client;

import br.com.tech.challenge.api.client.factory.ClientHttpFactory;
import br.com.tech.challenge.api.client.factory.RetryTemplateFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

public class MercadoPagoClient implements QRClient {

    private final String ACCESS_TOKEN = "TEST-6621802098599609-102118-84e7d61f11ef646bbaf89a78e1bb2631-700064145";

    private final String MERCADO_PAGO_URL = "https://api.mercadopago.com";

    @Override
    public String generateQRCode() {
        RetryTemplate retryTemplate = RetryTemplateFactory.retryTemplate();
        RestTemplate restTemplate = new RestTemplate(ClientHttpFactory.clientHttpRequestFactory());

        ResponseEntity<String> responseEntity = retryTemplate
                .execute(retryContext -> restTemplate.postForEntity());
    }

    @Override
    public boolean payQRCode() {
        return false;
    }

}
