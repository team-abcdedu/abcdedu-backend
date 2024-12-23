package com.abcdedu_backend.homework.dto.request;

import jakarta.annotation.Nullable;

public record RepresentativeRegisterRequest(
        @Nullable
        Long homeworkId,
        Long memberId
) {
}
