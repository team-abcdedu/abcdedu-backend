package com.abcdedu_backend.post.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record PostResponse (
        String title,
        String writer,
        String content,
        LocalDateTime createdAt,
        Long viewCount,
        Long commentCount,
        String fileDownloadUrl,
        Boolean secret,
        Boolean commentAllow
){

}
