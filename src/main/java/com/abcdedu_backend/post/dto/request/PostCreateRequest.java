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
        @NotBlank
        String title,
        @NotNull
        String content,
        Long viewCount,
        @NotNull
        Boolean secret,
        @NotNull
        Boolean commentAllow,
        @NotBlank
        String boardName
){
        // 기본 설정 값 뺀 필드들을 파라미터로 받아 객체 생성
        public PostCreateRequest(String title, String content, Boolean secret, Boolean commentAllow, String boardName) {
                this(title, content, 0L, secret, commentAllow, boardName);
        }
}
