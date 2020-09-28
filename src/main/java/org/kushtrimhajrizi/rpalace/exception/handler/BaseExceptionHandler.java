package org.kushtrimhajrizi.rpalace.exception.handler;

import org.kushtrimhajrizi.rpalace.exception.AccessTokenCreationException;
import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.exception.RefreshTokenException;
import org.kushtrimhajrizi.rpalace.exception.UnauthorizedException;
import org.kushtrimhajrizi.rpalace.exception.UserAlreadyExistsException;
import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExistException;
import org.kushtrimhajrizi.rpalace.exception.ValidationException;
import org.kushtrimhajrizi.rpalace.exception.response.ExceptionResponse;
import org.kushtrimhajrizi.rpalace.exception.response.ValidationExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(AccessTokenCreationException.class)
    public ResponseEntity<ExceptionResponse> handleAccessTokenCreationException() {
        return getExceptionResponse("There was a problem while creating your access token",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<ExceptionResponse> handleAccessTokenException() {
        return getExceptionResponse("There was a problem with your access token",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ExceptionResponse> handleRefreshTokenException() {
        return getExceptionResponse("There was a problem with your refresh token",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> handleUnauthorizedException() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException() {
        return getExceptionResponse("User already exists",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ExceptionResponse> handleUserDoesNotExistException() {
        return getExceptionResponse("User does not exist",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationExceptionResponse> handleValidationException(
            ValidationException validationException) {
        BindingResult bindingResult = validationException.getBindingResult();
        ValidationExceptionResponse response = new ValidationExceptionResponse(
                "Validation error", Instant.now());
        bindingResult.getFieldErrors().stream()
                .forEach(fieldError -> response.addError(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException() {
        return getExceptionResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> getExceptionResponse(String message, HttpStatus status) {
        ExceptionResponse response = new ExceptionResponse(message, Instant.now());
        return new ResponseEntity<>(response, status);
    }
}
