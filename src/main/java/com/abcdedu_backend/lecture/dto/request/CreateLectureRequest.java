package com.abcdedu_backend.lecture.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateLectureRequest(
        @NotBlank
        String title,
        @NotBlank
        String subTitle,
        @NotBlank
        String description
) {
}
