package br.com.tech.challenge.api.client;

import br.com.tech.challenge.api.client.factory.ClientHttpFactory;
import br.com.tech.challenge.api.client.factory.RetryTemplateFactory;
import br.com.tech.challenge.domain.dto.external.MercadoPagoRequestDTO;
import br.com.tech.challenge.domain.dto.external.MercadoPagoResponseDTO;
import br.com.tech.challenge.domain.enums.MercadoPagoAPI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class MercadoPagoClient implements QRClient {

    private final RetryTemplate retryTemplate = RetryTemplateFactory.retryTemplate();
    private final RestTemplate restTemplate = new RestTemplate(ClientHttpFactory.clientHttpRequestFactory());

    @Override
    public MercadoPagoResponseDTO generateQRCode(MercadoPagoRequestDTO dto) {
        ResponseEntity<MercadoPagoResponseDTO> responseEntity = retryTemplate
                .execute(retryContext -> restTemplate.postForEntity(
                        MercadoPagoAPI.MERCADO_PAGO_URL.getValor().concat(MercadoPagoAPI.getQRCodeUrl()),
                        httpEntity(dto),
                        MercadoPagoResponseDTO.class));

        return responseEntity.getBody();
    }

    @Override
    public boolean payQRCode(MercadoPagoRequestDTO dto) {
        try {
            retryTemplate.execute(retryContext -> restTemplate.exchange(
                    MercadoPagoAPI.MERCADO_PAGO_URL.getValor().concat(MercadoPagoAPI.getQRCodeUrl()),
                    HttpMethod.PUT,
                    httpEntity(dto),
                    Void.class
            ));
            return true;
        } catch (RestClientException exception) {
            return false;
        }
    }

    private HttpEntity<MercadoPagoRequestDTO> httpEntity(MercadoPagoRequestDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", MercadoPagoAPI.ACCESS_TOKEN.getValor());

        return new HttpEntity<>(dto, headers);
    }

}
