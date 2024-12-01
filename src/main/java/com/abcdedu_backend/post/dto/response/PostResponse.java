package com.abcdedu_backend.post.dto.response;

import com.abcdedu_backend.post.dto.PostNavigation;
import com.abcdedu_backend.post.entity.Post;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record PostResponse (
        String title,
        String writer,
        String writerEmail,
        String content,
        LocalDateTime createdAt,
        Long viewCount,
        Long commentCount,
        String fileUrl,
        Boolean secret,
        Boolean commentAllow,
        PostNavigation prev,
        PostNavigation next
){

    public static PostResponse of(Post post, Post prev, Post next, String fileUrl) {
        return PostResponse.builder()
                .title(post.getTitle())
                .writer(post.getMember().getName())
                .writerEmail(post.getMember().getEmail())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .fileUrl(fileUrl)
                .secret(post.getSecret())
                .commentAllow(post.getCommentAllow())
                .prev(PostNavigation.of(prev))
                .next(PostNavigation.of(next))
                .build();

    }
}
