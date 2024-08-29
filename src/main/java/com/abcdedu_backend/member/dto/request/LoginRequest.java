package com.abcdedu_backend.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email
        @NotBlank(message = "값이 비어있습니다.")
        String email,
        @Size(min = 6, max = 20, message = "패스워드의 길이는 6~20이어야 합니다.")
        @NotBlank(message = "값이 비어있습니다.")
        String password
) {
}
