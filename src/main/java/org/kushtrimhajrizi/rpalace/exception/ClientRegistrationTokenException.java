package org.kushtrimhajrizi.rpalace.exception;

/**
 * @author Kushtrim Hajrizi
 */
public class ClientRegistrationTokenException extends RuntimeException {

    public ClientRegistrationTokenException(String message) {
        super(message);
    }

    public ClientRegistrationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
