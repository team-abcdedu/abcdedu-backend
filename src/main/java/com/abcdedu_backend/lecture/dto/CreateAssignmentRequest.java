package com.abcdedu_backend.lecture.dto;


import java.util.List;

public record CreateAssignmentRequest(
        String title,
        String body,
        List<CreateAssignmentQuestionsDto> questions

) {
}
