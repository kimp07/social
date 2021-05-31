package org.senlacourse.social.dto.validation;

import lombok.experimental.UtilityClass;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

@UtilityClass
public class DtoValidator {

    public <T> Set<ConstraintViolation<T>> validate(T dto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator().validate(dto);
    }

    public <T> String getErrorMessage(Set<ConstraintViolation<T>> constraints) {
        StringBuilder messages = new StringBuilder();
        constraints.forEach(
                c -> messages
                        .append(c.getPropertyPath())
                        .append(" ")
                        .append(c.getMessage())
                        .append("\n"));
        return messages.toString();
    }
}
