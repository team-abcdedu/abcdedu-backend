package com.abcdedu_backend.post.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostUpdateRequest (
        @NotBlank
        String title,
        String content,
        // MultipartFile file, //  첨부파일
        Boolean secret,
        Boolean commentAllow
) {
}
