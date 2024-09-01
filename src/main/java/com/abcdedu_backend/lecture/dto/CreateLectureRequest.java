package com.abcdedu_backend.lecture.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateLectureRequest(
        @NotBlank
        String title,
        @NotBlank
        String type,
        @NotBlank
        String description
) {
}
