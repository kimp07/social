package org.senlacourse.social.api.util;

import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;

@UtilityClass
public class ValidationErrorMessagesUtil {

    public String getErrorsMessage(BindingResult bindingResult) {
        StringBuilder messages = new StringBuilder();
        bindingResult.getAllErrors().forEach(e -> messages.append(e.getDefaultMessage()).append("\n"));
        return messages.toString();
    }
}
