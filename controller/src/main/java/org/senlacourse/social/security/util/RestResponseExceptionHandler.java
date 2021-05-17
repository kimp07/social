package org.senlacourse.social.security.util;

import org.senlacourse.social.api.exception.DtoValidationException;
import org.senlacourse.social.api.exception.ObjectNotFoundException;
import org.senlacourse.social.api.exception.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ObjectNotFoundException.class})
    public ResponseEntity<Object> handleObjectNotFoundException(Exception ex) {
        return new ResponseEntity<>(
                ex, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DtoValidationException.class})
    public ResponseEntity<Object> handleBadRequestBodyException(Exception ex) {
        return new ResponseEntity<>(
                ex, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<Object> handleServiceException(Exception ex) {
        return new ResponseEntity<>(
                ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
