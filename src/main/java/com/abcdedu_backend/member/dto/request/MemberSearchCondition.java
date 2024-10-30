package com.abcdedu_backend.member.dto.request;

import com.abcdedu_backend.member.entity.MemberRole;

public record MemberSearchCondition(
        String school,
        Long studentId,
        String name,
        MemberRole role

) {

}
