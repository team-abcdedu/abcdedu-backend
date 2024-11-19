package com.abcdedu_backend.member.domain;

import lombok.Builder;

@Builder
public record LoginToken(
        String accessToken,
        String refreshToken
) {
}
