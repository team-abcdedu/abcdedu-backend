package com.abcdedu_backend.member.dto.response;

import lombok.Builder;


@Builder
public record MemberShortInfoResponse (
        String name,
        String role
) {
}

