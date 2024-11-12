package com.abcdedu_backend.memberv2.application.domain;

import lombok.Builder;

@Builder
public record LoginToken(
        String accessToken,
        String refreshToken
) {
}
