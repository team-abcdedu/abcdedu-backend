package com.abcdedu_backend.member.dto.request;

public record UpdateMemberInfoRequest(
        String name,
        String school,
        Long studentId
) {
}
