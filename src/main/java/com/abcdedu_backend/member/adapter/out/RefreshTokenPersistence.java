package com.abcdedu_backend.member.adapter.out;

import com.abcdedu_backend.member.adapter.out.entity.RefreshTokenEntity;
import com.abcdedu_backend.member.domain.RefreshToken;
import com.abcdedu_backend.member.application.out.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class RefreshTokenPersistence implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenJpaRepository.save(new RefreshTokenEntity(refreshToken.getToken(), refreshToken.getId()));
        return refreshTokenEntity.toDomain();
    }

    @Override
    public Optional<RefreshToken> findById(String refreshToken) {
        return refreshTokenJpaRepository.findById(refreshToken).map(RefreshTokenEntity::toDomain);
    }

    @Override
    public void deleteById(String refreshToken) {
        refreshTokenJpaRepository.deleteById(refreshToken);
    }
}
