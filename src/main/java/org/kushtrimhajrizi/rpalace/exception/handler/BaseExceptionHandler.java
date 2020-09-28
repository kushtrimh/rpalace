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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class BaseExceptionHandler {

    private MessageSource messageSource;

    private BaseExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AccessTokenCreationException.class)
    public ResponseEntity<ExceptionResponse> handleAccessTokenCreationException() {
        return getLocalizedExceptionResponse("exception.access_token_creation",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessTokenException.class)
    public ResponseEntity<ExceptionResponse> handleAccessTokenException() {
        return getLocalizedExceptionResponse("exception.access_token",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ExceptionResponse> handleRefreshTokenException() {
        return getLocalizedExceptionResponse("exception.refresh_Token",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> handleUnauthorizedException() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException() {
        return getLocalizedExceptionResponse("exception.user_exists",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ExceptionResponse> handleUserDoesNotExistException() {
        return getLocalizedExceptionResponse("exception.user_does_not_exist",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationExceptionResponse> handleValidationException(
            ValidationException validationException) {
        BindingResult bindingResult = validationException.getBindingResult();
        ValidationExceptionResponse response = new ValidationExceptionResponse(
                messageSource.getMessage("exception.validation", null, LocaleContextHolder.getLocale()),
                Instant.now());
        bindingResult.getFieldErrors().stream()
                .forEach(fieldError -> response.addError(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException() {
        return getLocalizedExceptionResponse("exception.general", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> getExceptionResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(new ExceptionResponse(message, Instant.now()), status);
    }

    private ResponseEntity<ExceptionResponse> getLocalizedExceptionResponse(String messageKey, HttpStatus status) {
        String message = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new ExceptionResponse(message, Instant.now()), status);
    }
}
