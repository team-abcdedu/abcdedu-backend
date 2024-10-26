package com.abcdedu_backend.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomCodeGenerator {

    private static final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int CODE_LENGTH = 6;

    public String generateAuthCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder authCode = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHAR_SET.length());
            authCode.append(CHAR_SET.charAt(randomIndex));
        }

        return authCode.toString();
    }
}
