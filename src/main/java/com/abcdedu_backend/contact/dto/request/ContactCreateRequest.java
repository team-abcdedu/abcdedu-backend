package com.abcdedu_backend.contact.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactCreateRequest (
        @NotBlank
        String type,
        @NotBlank
        String title,
        @NotBlank
        String userName,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String content
)
{
}
