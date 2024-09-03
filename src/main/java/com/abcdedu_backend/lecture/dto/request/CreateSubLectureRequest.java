package com.abcdedu_backend.lecture.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSubLectureRequest(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        Integer orderNumber
) {
}
