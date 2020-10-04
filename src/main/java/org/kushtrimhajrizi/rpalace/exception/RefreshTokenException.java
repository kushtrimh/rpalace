package org.kushtrimhajrizi.rpalace.exception;

public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException() {
    }

    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
