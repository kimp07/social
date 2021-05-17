package org.senlacourse.social.security.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestBodyException extends RuntimeException {

    public BadRequestBodyException(String message) {
        super(message);
    }

    public BadRequestBodyException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestBodyException(Throwable cause) {
        super(cause);
    }

    public BadRequestBodyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}