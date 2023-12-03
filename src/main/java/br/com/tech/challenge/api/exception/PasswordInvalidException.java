package br.com.tech.challenge.api.exception;

public class PasswordInvalidException extends RuntimeException {

    public PasswordInvalidException() {
        super("A senha informada é inválida.");
    }

}
