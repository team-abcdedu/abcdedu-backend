package com.abcdedu_backend.post.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(String content, String writerName, String writerEmail, LocalDateTime createdAt) {
}
