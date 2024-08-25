package com.abcdedu_backend.post.dto.response;

import com.abcdedu_backend.post.Post;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 게시글 목록 보는 Response
 */
@Getter
@Builder
public class PostListResponse {
    private String title;
    private String writer;
    private Long viewCount;
    private Long commentCount;
    private LocalDateTime updatedAt;


    public static PostListResponse of(Post post) {
        return PostListResponse.builder()
                .title(post.getTitle())
                .writer(post.getMember().getName())
                .viewCount(post.getViewCount())
                .commentCount((long) post.getComments().size())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
