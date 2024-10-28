package com.abcdedu_backend.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SendEmailCodeRequest (
        @Email
        @NotNull
        String email
){
}
