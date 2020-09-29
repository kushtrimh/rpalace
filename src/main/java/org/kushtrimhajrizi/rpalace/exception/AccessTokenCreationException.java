package org.kushtrimhajrizi.rpalace.exception;

/**
 * @author Kushtrim Hajrizi
 */
public class AccessTokenCreationException extends AccessTokenException {

    public AccessTokenCreationException(String message) {
        super(message);
    }

    public AccessTokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
