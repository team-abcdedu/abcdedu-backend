package com.abcdedu_backend.memberv2.application.dto;

import com.abcdedu_backend.memberv2.application.domain.Member;

public record MemberBasicInfoDto(
        String name,
        String role,
        String email
){
    public static MemberBasicInfoDto of(Member member) {
        return new MemberBasicInfoDto(member.getName(), member.getRole().getName(), member.getEmail());
    }
}