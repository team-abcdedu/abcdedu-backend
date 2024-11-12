package com.abcdedu_backend.memberv2.adapter.in.dto.response;

import com.abcdedu_backend.memberv2.application.dto.MemberBasicInfoDto;
import lombok.Builder;

@Builder
public record MemberBasicInfoResponse (
        String name,
        String role,
        String email
){
    public static MemberBasicInfoResponse of(MemberBasicInfoDto memberBasicInfoDto) {
        return new MemberBasicInfoResponse(memberBasicInfoDto.name(), memberBasicInfoDto.role(), memberBasicInfoDto.email());
    }
}
