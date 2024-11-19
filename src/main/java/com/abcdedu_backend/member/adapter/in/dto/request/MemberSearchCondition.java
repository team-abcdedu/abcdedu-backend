package com.abcdedu_backend.member.adapter.in.dto.request;

import com.abcdedu_backend.member.domain.MemberRole;

public record MemberSearchCondition(
        String school,
        Long studentId,
        String name,
        MemberRole role

) {

}
