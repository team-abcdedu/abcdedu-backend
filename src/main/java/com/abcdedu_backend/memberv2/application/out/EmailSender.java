package com.abcdedu_backend.memberv2.application.out;

public interface EmailSender {
    void sendTempPassword(String toEmail, String tempPassword);

    void sendCode(String toEmail, String code);
}
