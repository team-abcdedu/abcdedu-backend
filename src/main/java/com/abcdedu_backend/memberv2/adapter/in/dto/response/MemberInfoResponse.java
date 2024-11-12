package com.abcdedu_backend.memberv2.adapter.in.dto.response;

import com.abcdedu_backend.memberv2.application.dto.MemberInfoDto;
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
    public static MemberInfoResponse of(MemberInfoDto memberInfoDto) {
        return MemberInfoResponse.builder()
                .studentId(memberInfoDto.studentId())
                .email(memberInfoDto.email())
                .name(memberInfoDto.name())
                .role(memberInfoDto.role())
                .school(memberInfoDto.school())
                .imageUrl(memberInfoDto.imageUrl())
                .createdAt(memberInfoDto.createdAt())
                .createPostCount(memberInfoDto.createPostCount())
                .createCommentCount(memberInfoDto.createCommentCount())
                .build();
    }

}
