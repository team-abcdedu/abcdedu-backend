package com.abcdedu_backend.member.application.dto;

import com.abcdedu_backend.member.domain.Member;

public record MemberBasicInfoDto(
        String name,
        String role,
        String email
){
    public static MemberBasicInfoDto of(Member member) {
        return new MemberBasicInfoDto(member.getName(), member.getRole().getName(), member.getEmail());
    }
}
