package com.abcdedu_backend.lecture.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetAssignmentAnswerResponse (
        Long assignmentId,
        Long subClassId,
        String subClassName,
        LocalDateTime updatedAt,
        String userName
){
}
