package com.abcdedu_backend.lecture.dto;

import lombok.Builder;

@Builder
public record QuestionsDto(
        String title,
        String body,
        Integer orderNumber,
        String assignmentAnswerType
){

}
