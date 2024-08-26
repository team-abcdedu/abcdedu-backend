package com.abcdedu_backend.member.controller.dto.response;

import lombok.Builder;

@Builder
public record MemberInfoResponse(
        String name,
        String email,
        String school,
        Long studentId,
        String imageUrl,
        String role
) {

}
