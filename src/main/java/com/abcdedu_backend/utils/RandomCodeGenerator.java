package com.abcdedu_backend.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomCodeGenerator {

    private static final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int CODE_LENGTH = 6;
    private static final int PASSWORD_LENGTH = 20;

    public String generateAuthCode() {
        return generateNum(CODE_LENGTH);
    }

    public String generatePassword() {
        return generateNum(PASSWORD_LENGTH);
    }

    private String generateNum(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder authCode = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHAR_SET.length());
            authCode.append(CHAR_SET.charAt(randomIndex));
        }

        return authCode.toString();
    }
}
