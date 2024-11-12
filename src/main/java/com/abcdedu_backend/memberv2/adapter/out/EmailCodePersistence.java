package com.abcdedu_backend.memberv2.adapter.out;

import com.abcdedu_backend.memberv2.adapter.out.entity.EmailCodeEntity;
import com.abcdedu_backend.memberv2.application.domain.EmailCode;
import com.abcdedu_backend.memberv2.application.out.EmailCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class EmailCodePersistence implements EmailCodeRepository {

    private final EmailCodeJpaRepository emailCodeJpaRepository;

    @Override
    public void save(EmailCode emailCode) {
        emailCodeJpaRepository.save(new EmailCodeEntity(emailCode.email(), emailCode.code()));
    }

    @Override
    public Optional<EmailCode> findById(String email) {
        return emailCodeJpaRepository.findById(email).map(EmailCodeEntity::toDomain);
    }
}
