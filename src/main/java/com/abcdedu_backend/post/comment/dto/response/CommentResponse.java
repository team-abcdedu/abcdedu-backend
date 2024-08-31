package com.abcdedu_backend.post.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentResponse(String content, String writerName) {
}
