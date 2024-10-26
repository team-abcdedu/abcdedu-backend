package com.abcdedu_backend.member.dto.request;

public record MemberSearchCondition(
        String school,
        Long studentId,
        String name,
        String role

) {

}
