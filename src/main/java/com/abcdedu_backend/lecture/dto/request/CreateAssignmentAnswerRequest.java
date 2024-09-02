package com.abcdedu_backend.lecture.dto.request;

import com.abcdedu_backend.lecture.dto.CreateAssignmentAnswerDto;

import java.util.List;

public record CreateAssignmentAnswerRequest (
        List<CreateAssignmentAnswerDto> answers
){
}
