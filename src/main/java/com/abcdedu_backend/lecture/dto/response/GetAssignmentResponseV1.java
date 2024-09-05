package com.abcdedu_backend.lecture.dto.response;

public record GetAssignmentResponseV1 (
        String assignmentType,
        Long assignmentFileId
){
}
