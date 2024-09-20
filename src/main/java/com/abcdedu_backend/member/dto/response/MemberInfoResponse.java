package com.abcdedu_backend.member.dto.response;

import com.abcdedu_backend.member.entity.Member;
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
    public static MemberInfoResponse of(Member member, String imageUrl) {
        return MemberInfoResponse.builder()
                .studentId(member.getStudentId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole().getName())
                .school(member.getSchool())
                .imageUrl(imageUrl)
                .createdAt(member.getCreatedAt())
                .createPostCount(member.getPosts().size())
                .createCommentCount(member.getComments().size())
                .build();
    }

}
