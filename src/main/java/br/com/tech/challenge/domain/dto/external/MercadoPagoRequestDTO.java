package br.com.tech.challenge.domain.dto.external;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MercadoPagoRequestDTO {

    private String externalReference;

    private String title;

    private String description;

    private BigDecimal totalAmount;

    private List<ItemDTO> items;

    private CashOutDTO cashOut;

}
