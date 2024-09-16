package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void 토큰저장_성공() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
                .token("refreshToken")
                .id(1L)
                .build();

        //when
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        //then
        assertThat(savedRefreshToken.getToken()).isEqualTo("refreshToken");
        assertThat(savedRefreshToken.getId()).isEqualTo(1L);
    }

    @Test
    void 토큰삭제_성공() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
                .token("refreshToken")
                .id(1L)
                .build();
        refreshTokenRepository.save(refreshToken);

        //when
        refreshTokenRepository.deleteById("refreshToken");

        //then
        Optional<RefreshToken> deletedToken = refreshTokenRepository.findById(refreshToken.getToken());
        assertThat(deletedToken).isEmpty();
    }

}
