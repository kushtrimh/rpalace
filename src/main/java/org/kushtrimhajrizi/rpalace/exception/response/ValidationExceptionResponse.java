package org.kushtrimhajrizi.rpalace.exception.response;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ValidationExceptionResponse extends ExceptionResponse {

    private List<ValidationError> validationErrors = new ArrayList<>();

    public ValidationExceptionResponse(String message, Instant timestamp) {
        super(message, timestamp);
    }

    public void addError(String field, String message) {
        validationErrors.add(new ValidationError(field, message));
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationExceptionResponse that = (ValidationExceptionResponse) o;
        return Objects.equals(validationErrors, that.validationErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationErrors);
    }

    @Override
    public String toString() {
        return "ValidationExceptionResponse{" +
                ", validationErrors=" + validationErrors +
                '}';
    }

    private class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "ValidationError{" +
                    "field='" + field + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
