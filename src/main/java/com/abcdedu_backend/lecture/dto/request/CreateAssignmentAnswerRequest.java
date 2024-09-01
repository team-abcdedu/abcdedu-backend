package com.abcdedu_backend.lecture.dto.request;

import com.abcdedu_backend.lecture.dto.CreateAssignmentAnswerDto;

import java.util.List;

public record CreateAssignmentAnswerRequest (
        String school,
        Long studentId,
        String name,
        List<CreateAssignmentAnswerDto> answers
){
}
