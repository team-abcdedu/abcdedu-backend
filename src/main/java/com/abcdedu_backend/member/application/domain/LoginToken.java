package com.abcdedu_backend.member.application.domain;

import lombok.Builder;

@Builder
public record LoginToken(
        String accessToken,
        String refreshToken
) {
}
