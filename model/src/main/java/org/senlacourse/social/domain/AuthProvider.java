package org.senlacourse.social.domain;

public enum AuthProvider {

    LOCAL,
    GOOGLE,
    GITHUB;

    public static AuthProvider getByOrdinal(int ordinal) {
        AuthProvider authProvider = null;
        AuthProvider[] types = AuthProvider.values();
        if (ordinal >= 0 && ordinal < types.length) {
            authProvider = types[ordinal];
        }
        return authProvider;
    }

    public String getLowerCaseValue() {
        return name().toLowerCase();
    }
}