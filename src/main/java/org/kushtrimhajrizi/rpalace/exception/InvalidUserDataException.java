package org.kushtrimhajrizi.rpalace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidUserDataException extends RuntimeException {

    public InvalidUserDataException() {}
    public InvalidUserDataException(String msg) {
        super(msg);
    }
}
