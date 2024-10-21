package com.abcdedu_backend.lecture.dto.response;

import com.abcdedu_backend.lecture.entity.AssignmentFile;

public record GetAssignmentResponseV1 (
        String assignmentType,
        Long assignmentFileId
){

    public static GetAssignmentResponseV1 of(AssignmentFile assignmentFile) {
        return new GetAssignmentResponseV1(
                assignmentFile.getAssignmentType().getType(),
                assignmentFile.getId()
        );
    }
}
