package com.abcdedu_backend.memberv2.application.dto;

import com.abcdedu_backend.memberv2.application.domain.Member;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberInfoDto (
        String name,
        String email,
        String school,
        Long studentId,
        String imageUrl,
        String role,
        LocalDateTime createdAt,
        Integer createPostCount,
        Integer createCommentCount
){
    public static MemberInfoDto of(Member member, String imageUrl) {
        return MemberInfoDto.builder()
                .studentId(member.getStudentId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole().getName())
                .school(member.getSchool())
                .imageUrl(imageUrl)
                .createdAt(member.getCreatedAt())
                .createPostCount(member.getPostCount())
                .createCommentCount(member.getCommentCount())
                .build();
    }
}
