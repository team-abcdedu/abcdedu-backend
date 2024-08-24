package com.abcdedu_backend.member.controller.dto;

import lombok.Builder;

@Builder
public record LoginTokenDTO(
        String accessToken,
        String refreshToken
) {
}
