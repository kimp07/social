package org.senlacourse.social.security.rest;

import org.senlacourse.social.security.util.BadRequestBodyException;
import org.senlacourse.social.security.util.ValidationErrorMessagesUtil;
import org.springframework.validation.BindingResult;

public abstract class AbstractController {

    protected void validateRequestBody(BindingResult bindingResult) throws BadRequestBodyException {
        if (bindingResult.hasErrors()) {
            throw new BadRequestBodyException(ValidationErrorMessagesUtil.getErrorsMessage(bindingResult));
        }
    }
}
