package com.abcdedu_backend.member.adapter.out;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.application.out.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {

    private final JavaMailSender emailSender;

    private static final String SEND_CODE_TITLE = "[ABCDEdu] 회원가입 인증번호 안내 메일입니다.";
    private static final String SEND_CODE_TEXT_FORM = "인증번호는 <b>%s</b> 입니다.";
    private static final String SEND_TEMP_PASSWORD_TITLE = "[ABCDEdu] 비밀번호 찾기 안내 메일입니다.";
    private static final String SEND_TEMP_PASSWORD_TEXT_FORM = "임시 비밀번호는 <b>%s</b> 입니다.";

    private static final String ENCODING_BASE = "UTF-8";

    @Async
    @Override
    public void sendTempPassword(String toEmail, String tempPassword) {
        String text = String.format(SEND_TEMP_PASSWORD_TEXT_FORM, tempPassword);

        try {
            MimeMessage emailForm = createEmailForm(toEmail, SEND_TEMP_PASSWORD_TITLE, text);
            emailSender.send(emailForm);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Async
    @Override
    public void sendCode(String toEmail, String code) {
        String text = String.format(SEND_CODE_TEXT_FORM, code);

        try {
            MimeMessage emailForm = createEmailForm(toEmail, SEND_CODE_TITLE, text);
            emailSender.send(emailForm);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private MimeMessage createEmailForm(String toEmail, String title, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, ENCODING_BASE);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(text, true);

        return message;
    }
}
