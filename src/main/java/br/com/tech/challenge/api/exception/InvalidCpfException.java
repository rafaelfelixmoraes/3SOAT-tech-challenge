package br.com.tech.challenge.api.exception;

public class InvalidCpfException extends RuntimeException {

    public InvalidCpfException(String message) {
        super(message);
    }

}
