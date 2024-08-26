package com.abcdedu_backend.post.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record PostCreateRequest (
        @NotBlank
        String title,
        @NotNull
        String content,
        Long viewCount,
        Boolean secret,
        Boolean commentAllow,
        @NotNull
        String boardName
){

}
