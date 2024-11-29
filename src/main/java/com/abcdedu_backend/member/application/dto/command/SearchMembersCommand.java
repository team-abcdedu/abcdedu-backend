package com.abcdedu_backend.member.application.dto.command;

import com.abcdedu_backend.member.domain.MemberRole;

public record SearchMembersCommand(
        String school,
        Long studentId,
        String name,
        MemberRole role
){
    public static SearchMembersCommand of(String school, Long studentId, String name, MemberRole role) {
        return new SearchMembersCommand(school, studentId, name, role);
    }
}
