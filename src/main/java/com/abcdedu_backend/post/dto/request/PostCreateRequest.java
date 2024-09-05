package com.abcdedu_backend.post.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 처음 글을 작성할 때, 기본값으로 설정 되는 필드
 * - viewCount = 0        조회수
 */
@Slf4j
public record PostCreateRequest (
        @NotNull
        Long boardId,
        @NotBlank
        String title,
        @NotNull
        String content,
        @NotNull
        Boolean secret,
        @NotNull
        Boolean commentAllow
){
}
