package org.kushtrimhajrizi.rpalace.exception;

public class UserAlreadyHasToken extends Exception {

    public UserAlreadyHasToken() {
    }

    public UserAlreadyHasToken(String message) {
        super(message);
    }
}
