package com.abcdedu_backend.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CommentUpdateRequest(
        @Schema(example = "해당 내용으로 댓글 내용을 수정합니다.")
        @NotNull
        String content) {
}