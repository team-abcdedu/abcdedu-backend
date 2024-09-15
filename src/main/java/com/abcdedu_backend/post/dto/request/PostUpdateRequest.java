package com.abcdedu_backend.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest (
        @NotBlank
        @Schema(example = "게시글 제목을 이 내용으로 수정합니다.", description = "빈 내용을 입력해선 안됩니다.")
        String title,
        @NotNull
        @Schema(example = "게시글 내용을 이 내용으로 수정합니다.")
        String content,
        @NotNull
        @Schema(example = "true", description = "비밀글 여부")
        Boolean secret,

        @NotNull
        @Schema(example = "true", description = "비밀글 여부")
        Boolean commentAllow
) {
}
