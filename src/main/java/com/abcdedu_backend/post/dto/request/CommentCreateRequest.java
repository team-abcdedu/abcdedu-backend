package com.abcdedu_backend.post.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotNull
        String content
) {
}