package org.senlacourse.social.controller.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;

@UtilityClass
public class RequestBodyValidation {

    public <T> boolean fieldNotExists(Class<T> type, String field) {
        Field[] fields = type.getDeclaredFields();
        return Arrays
                .stream(fields)
                .noneMatch(f -> field.equals(f.getName()));
    }
}