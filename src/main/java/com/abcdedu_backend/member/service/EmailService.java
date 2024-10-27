package com.abcdedu_backend.member.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.EmailCode;
import com.abcdedu_backend.member.repository.EmailCodeRepository;
import com.abcdedu_backend.utils.RandomCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final RandomCodeGenerator randomCodeGenerator;
    private final EmailCodeRepository emailCodeRepository;

    private static final String TITLE = "[ABCDEdu] 회원 가입 인증 이메일 입니다.";
    private static final String TEXT_FORM = "인증번호는 <b>%s</b> 입니다.";

    public void sendCodeToEmail(String toEmail) {
        String code = randomCodeGenerator.generateAuthCode();
        String text = String.format(TEXT_FORM, code);

        try {
            MimeMessage emailForm = createEmailForm(toEmail, TITLE, text);
            emailSender.send(emailForm);
            emailCodeRepository.save(new EmailCode(toEmail, code));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public void checkCode(String email, String code) {
        EmailCode emailCode = emailCodeRepository.findById(email)
                .orElseThrow(() -> new ApplicationException(ErrorCode.EMAIL_CODE_NOT_FOUND));

        if (!Objects.equals(code, emailCode.getCode())) {
            throw new ApplicationException(ErrorCode.CODE_MISMATCH);
        }
    }

    private MimeMessage createEmailForm(String toEmail, String title, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(text, true);

        return message;
    }
}
