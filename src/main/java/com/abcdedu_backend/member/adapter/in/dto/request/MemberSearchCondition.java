package com.abcdedu_backend.member.adapter.in.dto.request;

import com.abcdedu_backend.member.application.dto.command.SearchMembersCommand;
import com.abcdedu_backend.member.domain.MemberRole;

public record MemberSearchCondition(
        String school,
        Long studentId,
        String name,
        MemberRole role

) {
    public SearchMembersCommand toCommand() {
        return new SearchMembersCommand(school, studentId, name, role);
    }
}
