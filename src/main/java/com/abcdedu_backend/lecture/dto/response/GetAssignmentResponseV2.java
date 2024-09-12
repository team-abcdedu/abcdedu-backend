package com.abcdedu_backend.lecture.dto.response;

import com.abcdedu_backend.lecture.dto.QuestionsDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetAssignmentResponseV2(
        String title,
        String body,
        List<QuestionsDto> questions
){
    public static GetAssignmentResponseV2 of(String title, String body, List<QuestionsDto> questions){
        return GetAssignmentResponseV2.builder()
                .title(title)
                .body(body)
                .questions(questions)
                .build();
    }
}
