package org.senlacourse.social.security.util;

import org.springframework.validation.BindingResult;

public class ValidationErrorMessagesUtil {

    private ValidationErrorMessagesUtil() {}

    public static String getErrorsMessage(BindingResult bindingResult) {
        StringBuilder messages = new StringBuilder();
        bindingResult.getAllErrors().forEach(e -> messages.append(e.getDefaultMessage()).append("\n"));
        return messages.toString();
    }
}
