package br.com.tech.challenge.domain.dto;

import br.com.tech.challenge.domain.QRDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MercadoPagoResponseDTO {

    private Long id;

    private QRDTO qr;

    private String status;

    private String dateCreated;

    private String dateLastUpdated;

    private String uuid;

    private Long userId;

    private String name;

    private Boolean fixedAmount;

    private Integer category;

    private String storeId;

    private String externalStoreId;

    private String site;

    private String qrCode;

}
