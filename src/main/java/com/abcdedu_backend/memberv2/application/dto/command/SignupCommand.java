package com.abcdedu_backend.memberv2.application.dto.command;

public record SignupCommand(
        String name,
        String email,
        String password,
        String school,
        Long studentId
) {
}
