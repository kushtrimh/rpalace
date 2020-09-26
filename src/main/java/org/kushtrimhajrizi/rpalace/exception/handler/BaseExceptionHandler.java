package org.kushtrimhajrizi.rpalace.exception.handler;

import org.kushtrimhajrizi.rpalace.exception.ValidationException;
import org.kushtrimhajrizi.rpalace.exception.response.ValidationExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationExceptionResponse> handleValidationException(
            ValidationException validationException) {
        BindingResult bindingResult = validationException.getBindingResult();
        ValidationExceptionResponse response = new ValidationExceptionResponse();
        response.setTimestamp(Instant.now());
        bindingResult.getFieldErrors().stream()
                .forEach(fieldError -> response.addError(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
