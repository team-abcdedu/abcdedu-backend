package com.abcdedu_backend.member.dto.response;

import lombok.Builder;

@Builder
public record MemberBasicInfoResponse (
        String name,
        String role,
        String email
){
}
