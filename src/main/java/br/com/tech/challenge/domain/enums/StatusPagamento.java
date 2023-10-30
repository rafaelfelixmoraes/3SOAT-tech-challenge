package br.com.tech.challenge.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusPagamento {

    AGUARDANDO_PAGAMENTO("Aguardando pagamento"),

    PAGO("Pago");

    final String descricao;

}
