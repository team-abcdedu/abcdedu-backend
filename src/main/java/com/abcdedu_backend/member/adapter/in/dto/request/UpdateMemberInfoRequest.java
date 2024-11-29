package com.abcdedu_backend.member.adapter.in.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemberInfoRequest(
        @NotBlank(message = "값이 비어있습니다.")
        String name,
        @Nullable
        String school,
        @Nullable
        Long studentId
) {
}
