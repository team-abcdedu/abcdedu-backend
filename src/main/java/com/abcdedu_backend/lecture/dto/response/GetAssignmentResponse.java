package com.abcdedu_backend.lecture.dto.response;

import com.abcdedu_backend.lecture.dto.QuestionsDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetAssignmentResponse (
        String title,
        String body,
        List<QuestionsDto> questions
){
    public static GetAssignmentResponse of(String title, String body, List<QuestionsDto> questions){
        return GetAssignmentResponse.builder()
                .title(title)
                .body(body)
                .questions(questions)
                .build();
    }
}
