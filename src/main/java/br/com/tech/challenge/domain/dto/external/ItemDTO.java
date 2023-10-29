package br.com.tech.challenge.domain.dto.external;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemDTO {

        private String category;

        private String title;

        private String description;

        private BigDecimal unitPrice;

        private Long quantity;

        private String unitMeasure;

        private BigDecimal totalAmount;

}
