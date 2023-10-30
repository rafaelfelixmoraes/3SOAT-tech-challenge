package br.com.tech.challenge.api.client;

import br.com.tech.challenge.domain.dto.external.MercadoPagoRequestDTO;
import br.com.tech.challenge.domain.dto.external.MercadoPagoResponseDTO;

public interface QRClient {

    MercadoPagoResponseDTO generateQRCode(MercadoPagoRequestDTO dto);

}
