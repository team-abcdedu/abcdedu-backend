package com.abcdedu_backend.memberv2.adapter.in.dto.response;

import lombok.Builder;


@Builder
public record MemberNameAndRoleResponse(
        String name,
        String role
) {
}

