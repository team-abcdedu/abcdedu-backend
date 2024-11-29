package com.abcdedu_backend.member.adapter.in.dto.response;


import com.abcdedu_backend.member.application.dto.MemberInfoDto;

import java.time.LocalDateTime;

public record AdminSearchMemberResponse(
        Long memberId,
        String name,
        String role,
        String email,
        String school,
        Long studentId,
        LocalDateTime createdAt
) {
    public static AdminSearchMemberResponse fromMemberInfoDto(MemberInfoDto m) {
        return new AdminSearchMemberResponse(
                m.id(),
                m.name(),
                m.role(),
                m.email(),
                m.school(),
                m.studentId(),
                m.createdAt());
    }

}
