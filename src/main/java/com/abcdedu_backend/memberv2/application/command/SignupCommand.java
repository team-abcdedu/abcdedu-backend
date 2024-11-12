package com.abcdedu_backend.memberv2.application.command;

public record SignupCommand(
        String name,
        String email,
        String password,
        String school,
        Long studentId
) {
}
