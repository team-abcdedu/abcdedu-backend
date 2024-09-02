package com.abcdedu_backend.post.dto.response;

import com.abcdedu_backend.post.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 상세 게시글 보기 전용
 */
@Builder
public record PostResponse (
        String title,
        String writer,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long viewCount,
        Long commentCount,
        List<Comment> comments
){

}
