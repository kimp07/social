package org.senlacourse.social.api.exception;

public class DtoValidationException extends Exception {

    public DtoValidationException(String message) {
        super(message);
    }

    public DtoValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DtoValidationException(Throwable cause) {
        super(cause);
    }
}
