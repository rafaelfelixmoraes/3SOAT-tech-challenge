package br.com.tech.challenge.api.client;

import br.com.tech.challenge.domain.dto.MercadoPagoRequestDTO;
import br.com.tech.challenge.domain.dto.MercadoPagoResponseDTO;

public interface QRClient {

    MercadoPagoResponseDTO generateQRCode(MercadoPagoRequestDTO dto);

    boolean payQRCode(MercadoPagoRequestDTO dto);

}
