package com.abcdedu_backend.member.application.out;

public interface EmailSender {
    void sendTempPassword(String toEmail, String tempPassword);

    void sendCode(String toEmail, String code);
}
