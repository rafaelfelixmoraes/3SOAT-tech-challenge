package br.com.tech.challenge.api.client.factory;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class ClientHttpFactory {

    public static ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(10000);
        factory.setConnectTimeout(6000);
        return factory;
    }

}
