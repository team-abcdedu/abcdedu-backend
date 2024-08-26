package com.abcdedu_backend.member.dto;

import lombok.Builder;

@Builder
public record LoginTokenDTO(
        String accessToken,
        String refreshToken
) {
}
