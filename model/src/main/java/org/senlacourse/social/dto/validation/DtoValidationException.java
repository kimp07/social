package org.senlacourse.social.dto.validation;

public class DtoValidationException extends RuntimeException {

    public DtoValidationException() {
        super();
    }

    public DtoValidationException(String message) {
        super(message);
    }

    public DtoValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DtoValidationException(Throwable cause) {
        super(cause);
    }

    protected DtoValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
