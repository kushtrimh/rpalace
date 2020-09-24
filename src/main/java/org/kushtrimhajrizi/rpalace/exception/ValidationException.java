package org.kushtrimhajrizi.rpalace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    private BindingResult bindingResult;

    public ValidationException() {}

    public ValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public ValidationException(String msg) {
        super(msg);
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
