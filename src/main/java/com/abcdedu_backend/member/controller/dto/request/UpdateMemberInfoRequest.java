package com.abcdedu_backend.member.controller.dto.request;

public record UpdateMemberInfoRequest(
        String name,
        String school,
        Long studentId
) {
}
