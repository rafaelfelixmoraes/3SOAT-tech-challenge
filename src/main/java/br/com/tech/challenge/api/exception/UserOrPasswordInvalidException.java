package br.com.tech.challenge.api.exception;

public class UserOrPasswordInvalidException extends RuntimeException {

    public UserOrPasswordInvalidException() {
        super("Usuário e/ou Senha inválidos");
    }

}
