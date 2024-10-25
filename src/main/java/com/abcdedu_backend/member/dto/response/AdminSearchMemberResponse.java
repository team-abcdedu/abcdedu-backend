package com.abcdedu_backend.member.dto.response;

import java.time.LocalDateTime;

public record AdminSearchMemberResponse (
    String name,
    String email,
    String school,
    Long StudentId,

    LocalDateTime createdAt
)
{

}
