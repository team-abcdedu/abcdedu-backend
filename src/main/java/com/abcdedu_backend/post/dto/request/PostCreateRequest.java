package com.abcdedu_backend.post.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

public record PostCreateRequest (
        @NotNull
        @Schema(example = "1")
        Long boardId,
        @NotBlank
        @Schema(example = "게시글 제목입니다.")
        String title,
        @NotNull
        @Schema(example = "게시글 내용입니다.")
        String content,
        @NotNull
        @Schema(example = "true", description = "비밀글여부를 설정합니다.")
        Boolean secret,
        @NotNull
        @Schema(example = "true", description = "댓글 허용 여부를 설정합니다.")
        Boolean commentAllow
){
}
