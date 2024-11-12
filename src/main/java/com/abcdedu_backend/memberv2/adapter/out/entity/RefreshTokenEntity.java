package com.abcdedu_backend.memberv2.adapter.out.entity;

import com.abcdedu_backend.memberv2.application.domain.RefreshToken;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@Builder
@RedisHash(value = "refreshToken", timeToLive = 1209600)
public class RefreshTokenEntity {

    @Id
    @NotNull
    private String token;

    @NotNull
    private Long id;

    public RefreshToken toDomain() {
        return new RefreshToken(token, id);
    }

}
