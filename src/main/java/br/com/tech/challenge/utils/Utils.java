package br.com.tech.challenge.utils;

import br.com.tech.challenge.api.exception.StatusPedidoInvalidoException;
import br.com.tech.challenge.domain.enums.StatusPedido;

public class Utils {

    public static void validarStatusPedido(StatusPedido statusPedido) {
        for (StatusPedido enumValue : StatusPedido.values()) {
            if (enumValue == statusPedido) {
                return;
            }
        }
        throw new StatusPedidoInvalidoException();
    }
}
