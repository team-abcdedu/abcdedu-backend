package com.abcdedu_backend.member.application.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RefreshToken {
    private String token;
    private Long id;
}
