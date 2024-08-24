package com.abcdedu_backend.member.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email
        @NotBlank
        String email,
        @Size(min = 6, max = 20)
        @NotBlank
        String password
) {
}
