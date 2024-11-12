package com.abcdedu_backend.memberv2.application.domain;

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
