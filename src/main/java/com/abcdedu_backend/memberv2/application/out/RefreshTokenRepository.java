package com.abcdedu_backend.memberv2.application.out;

import com.abcdedu_backend.memberv2.application.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findById(String refreshToken);

    void deleteById(String refreshToken);
}
