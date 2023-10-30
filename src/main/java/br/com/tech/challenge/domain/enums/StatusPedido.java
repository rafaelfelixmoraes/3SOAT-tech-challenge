package br.com.tech.challenge.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusPedido {

    RECEBIDO("Recebido"),

    EM_PREPARACAO("Em preparação"),

    PRONTO("Pronto"),

    FINALIZADO("Finalizado"),

    CANCELADO("Cancelado");

    final String descricao;

}
