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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final RandomCodeGenerator randomCodeGenerator;
    private final EmailCodeRepository emailCodeRepository;
    private final MemberService memberService;

    private static final String SEND_CODE_TITLE = "[ABCDEdu] 회원가입 인증번호 안내 메일입니다.";
    private static final String SEND_CODE_TEXT_FORM = "인증번호는 <b>%s</b> 입니다.";
    private static final String SEND_TEMP_PASSWORD_TITLE = "[ABCDEdu] 비밀번호 찾기 안내 메일입니다.";
    private static final String SEND_TEMP_PASSWORD_TEXT_FORM = "임시 비밀번호는 <b>%s</b> 입니다.";

    private static final String ENCODING_BASE = "UTF-8";

    @Async
    public void sendCodeToEmail(String toEmail) {
        String code = randomCodeGenerator.generateAuthCode();
        String text = String.format(SEND_CODE_TEXT_FORM, code);

        try {
            MimeMessage emailForm = createEmailForm(toEmail, SEND_CODE_TITLE, text);
            emailSender.send(emailForm);
            emailCodeRepository.save(new EmailCode(toEmail, code));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Async
    @Transactional
    public void sendTempPasswordToEmail(String toEmail) {
        String tempPassword = randomCodeGenerator.generatePassword();
        String text = String.format(SEND_TEMP_PASSWORD_TEXT_FORM, tempPassword);
        memberService.updatePassword(toEmail, tempPassword);

        try {
            MimeMessage emailForm = createEmailForm(toEmail, SEND_TEMP_PASSWORD_TITLE, text);
            emailSender.send(emailForm);
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
        MimeMessageHelper helper = new MimeMessageHelper(message, ENCODING_BASE);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(text, true);

        return message;
    }
}
