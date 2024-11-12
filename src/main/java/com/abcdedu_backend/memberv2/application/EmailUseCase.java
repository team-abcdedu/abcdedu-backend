package com.abcdedu_backend.memberv2.application;

public interface EmailUseCase {

    void sendTempPasswordToEmail(String email);

    void checkCode(String email, String code);

    void sendCodeToEmail(String email);
}
