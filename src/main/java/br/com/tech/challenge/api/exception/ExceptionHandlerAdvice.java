package br.com.tech.challenge.api.exception;

import br.com.tech.challenge.domain.dto.ResponseExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseExceptionDTO> handleConstraintViolationException(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        return ResponseEntity.badRequest().body(
                ResponseExceptionDTO.builder()
                        .exceptionMessage(exception.getMessage())
                        .messages(errors)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

}
