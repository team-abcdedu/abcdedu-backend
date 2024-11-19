package com.abcdedu_backend.member.application.out;

import com.abcdedu_backend.member.application.domain.EmailCode;

import java.util.Optional;

public interface EmailCodeRepository {
    void save(EmailCode emailCode);

    Optional<EmailCode> findById(String email);
}
