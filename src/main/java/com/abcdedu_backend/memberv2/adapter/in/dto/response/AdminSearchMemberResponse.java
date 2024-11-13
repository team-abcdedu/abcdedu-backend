package com.abcdedu_backend.memberv2.adapter.in.dto.response;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;

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
