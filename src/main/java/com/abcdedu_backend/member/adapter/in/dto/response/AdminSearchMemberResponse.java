package com.abcdedu_backend.member.adapter.in.dto.response;


import com.abcdedu_backend.member.domain.Member;
import com.abcdedu_backend.member.domain.MemberRole;

import java.time.LocalDateTime;

public record AdminSearchMemberResponse(
        Long memberId,
        String name,
        MemberRole role,
        String email,
        String school,
        Long studentId,
        LocalDateTime createdAt
) {
    public static AdminSearchMemberResponse fromMember(Member m) {
        return new AdminSearchMemberResponse(
                m.getId(),
                m.getName(),
                m.getRole(),
                m.getEmail(),
                m.getSchool(),
                m.getStudentId(),
                m.getCreatedAt());
    }

}
