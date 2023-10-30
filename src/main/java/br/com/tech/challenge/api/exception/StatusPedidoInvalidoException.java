package br.com.tech.challenge.api.exception;

public class StatusPedidoInvalidoException extends RuntimeException {
    public StatusPedidoInvalidoException() {
        super("Status inválido");
    }
}
