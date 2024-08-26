package com.abcdedu_backend.member.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberInfoResponse(
        String name,
        String email,
        String school,
        Long studentId,
        String imageUrl,
        String role,
        LocalDateTime createdAt,
        Integer createPostCount,
        Integer createCommentCount
) {

}
