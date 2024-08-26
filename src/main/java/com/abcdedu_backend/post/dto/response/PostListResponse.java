package com.abcdedu_backend.post.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

/**
 * 게시글 목록 보는 Response
 */
@Builder
public record PostListResponse (
        String title,
        String writer,
        Long viewCount,
        Long commentCount,
        LocalDateTime updatedAt
){
}
