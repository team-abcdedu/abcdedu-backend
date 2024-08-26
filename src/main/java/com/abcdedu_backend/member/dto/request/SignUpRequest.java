package com.abcdedu_backend.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank
        String name,
        @Email
        @NotBlank
        String email,
        @Size(min = 6, max = 20)
        @NotBlank
        String password
){
}
