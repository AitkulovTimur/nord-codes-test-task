package com.nordcodes.api.utils;

import java.security.SecureRandom;

public final class TokenGenerator {

    // По факту система принимает только заглавные hex-символы
    private static final String ALLOWED_CHARS = "0123456789ABCDEF";
    private static final int TOKEN_LENGTH = 32;
    private static final SecureRandom RANDOM = new SecureRandom();

    private TokenGenerator() {
    }

    public static String validToken() {
        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = RANDOM.nextInt(ALLOWED_CHARS.length());
            sb.append(ALLOWED_CHARS.charAt(index));
        }

        return sb.toString();
    }
}