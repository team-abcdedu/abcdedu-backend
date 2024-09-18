package com.abcdedu_backend.member.dto.response;

import lombok.Builder;


@Builder
public record MemberNameAndRoleResponse(
        String name,
        String role
) {
}

