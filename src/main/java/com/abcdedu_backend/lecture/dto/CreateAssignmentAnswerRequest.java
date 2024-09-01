package com.abcdedu_backend.lecture.dto;

import java.util.List;

public record CreateAssignmentAnswerRequest (
        String school,
        Long studentId,
        String name,
        List<CreateAssignmentAnswerDto> answers
){
}
