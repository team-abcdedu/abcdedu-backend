package com.abcdedu_backend.contact.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactCreateRequest (
        String type,
        @NotBlank
        String title,
        String userName,
        @Email
        @NotBlank
        String email,

        String phoneNumber,

        String content
)
{
}
