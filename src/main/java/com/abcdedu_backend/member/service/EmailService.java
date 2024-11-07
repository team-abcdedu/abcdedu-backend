package com.abcdedu_backend.member.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.EmailCode;
import com.abcdedu_backend.member.repository.EmailCodeRepository;
import com.abcdedu_backend.utils.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailSender emailSender;
    private final RandomCodeGenerator randomCodeGenerator;
    private final EmailCodeRepository emailCodeRepository;
    private final MemberService memberService;

    public void sendCodeToEmail(String toEmail) {
        String code = randomCodeGenerator.generateAuthCode();
        memberService.checkDuplicateEmail(toEmail);
        emailSender.sendCodeToEmail(code, toEmail);
        emailCodeRepository.save(new EmailCode(toEmail, code));
    }

    @Transactional
    public void sendTempPasswordToEmail(String toEmail) {
        String tempPassword = randomCodeGenerator.generatePassword();
        memberService.updatePassword(toEmail, tempPassword);
        emailSender.sendTempPasswordToEmail(tempPassword, toEmail);
    }

    public void checkCode(String email, String code) {
        EmailCode emailCode = emailCodeRepository.findById(email)
                .orElseThrow(() -> new ApplicationException(ErrorCode.EMAIL_CODE_NOT_FOUND));

        if (!Objects.equals(code, emailCode.getCode())) {
            throw new ApplicationException(ErrorCode.CODE_MISMATCH);
        }
    }
}
