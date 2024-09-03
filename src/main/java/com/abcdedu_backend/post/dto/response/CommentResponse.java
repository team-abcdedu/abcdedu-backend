package com.abcdedu_backend.post.dto.response;

import lombok.Builder;

@Builder
public record CommentResponse(String content, String writerName) {
}
