package com.abcdedu_backend.lecture.dto.request;


import com.abcdedu_backend.lecture.dto.QuestionsDto;

import java.util.List;

public record CreateAssignmentRequest(
        String title,
        String body,
        List<QuestionsDto> questions

) {
}
