package com.abcdedu_backend.memberv2.application;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.memberv2.application.domain.EmailCode;
import com.abcdedu_backend.memberv2.application.out.EmailCodeRepository;
import com.abcdedu_backend.memberv2.application.out.EmailSender;
import com.abcdedu_backend.memberv2.application.out.MemberRepository;
import com.abcdedu_backend.utils.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailUseCase{
    private final EmailSender emailSender;
    private final RandomCodeGenerator randomCodeGenerator;
    private final EmailCodeRepository emailCodeRepository;
    private final MemberInfoUseCase memberInfoUseCase;
    private final MemberRepository memberRepository;

    @Override
    public void sendCodeToEmail(String toEmail) {
        String code = randomCodeGenerator.generateAuthCode();
        if (memberRepository.checkDuplicateEmail(toEmail)){
            throw new ApplicationException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        emailSender.sendCode(toEmail, code);
        emailCodeRepository.save(new EmailCode(toEmail, code));
    }

    @Override
    @Transactional
    public void sendTempPasswordToEmail(String toEmail) {
        String tempPassword = randomCodeGenerator.generatePassword();
        memberInfoUseCase.updatePassword(toEmail, tempPassword);
        emailSender.sendTempPassword(toEmail, tempPassword);
    }

    @Override
    public void checkCode(String email, String code) {
        EmailCode emailCode = emailCodeRepository.findById(email)
                .orElseThrow(() -> new ApplicationException(ErrorCode.EMAIL_CODE_NOT_FOUND));

        if (!Objects.equals(code, emailCode.code())) {
            throw new ApplicationException(ErrorCode.CODE_MISMATCH);
        }
    }
}
