package br.com.tech.challenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MercadoPagoRequestDTO {

    private String name;

    @JsonProperty("fixed_amount")
    private Boolean fixedAmount;

    @JsonProperty("store_id")
    private Long storeId;

    @JsonProperty("external_store_id")
    private String externalStoreId;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("category")
    private Integer category;

}
