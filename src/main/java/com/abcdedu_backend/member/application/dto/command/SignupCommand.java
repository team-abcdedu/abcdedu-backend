package com.abcdedu_backend.member.application.dto.command;

public record SignupCommand(
        String name,
        String email,
        String password,
        String school,
        Long studentId
) {
}
