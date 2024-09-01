package com.abcdedu_backend.lecture.dto;

public record CreateAssignmentQuestionsRequest (
        String body,
        Integer orderNumber,
        String assignmentAnswerType
){

}
