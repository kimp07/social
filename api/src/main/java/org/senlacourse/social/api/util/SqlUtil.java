package org.senlacourse.social.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlUtil {

    public String normalizeLikeFilter(String value) {
        return "%" + value + "%";
    }
}
