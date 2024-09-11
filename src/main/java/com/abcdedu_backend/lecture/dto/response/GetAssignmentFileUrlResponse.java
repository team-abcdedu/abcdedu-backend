package com.abcdedu_backend.lecture.dto.response;

import lombok.Builder;

@Builder
public record GetAssignmentFileUrlResponse(
        String filePresignedUrl,
        Long assignmentAnswerFileId
) {
}
