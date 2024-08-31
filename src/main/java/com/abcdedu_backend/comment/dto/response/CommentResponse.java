package com.abcdedu_backend.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentResponse(String content, String writerName) {
}
