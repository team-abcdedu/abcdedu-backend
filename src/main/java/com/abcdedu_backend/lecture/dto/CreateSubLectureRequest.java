package com.abcdedu_backend.lecture.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSubLectureRequest(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank
        Integer OrderNumber
) {
}
