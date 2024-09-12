package com.abcdedu_backend.homework.dto;

import com.abcdedu_backend.homework.entity.HomeworkQuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class HomeworkQuestionReq {
    public record Create(
        @Schema(example = "SUBJECTIVE")
        @NotNull
        HomeworkQuestionType type,
        @Schema(example = "해결하고 싶은 현재 또는 미래 사회의 문제 찾기")
        @NotBlank
        String title,
        @Schema(example = " 평소에 해결하고 싶었던 어떠한 문제가 있는가? 또는 미래 사회에 우리가 부딪히게 될 문제 중 해결하고 싶은 무언가가 있는가?" +
            " 어떠한 종류의 문제든 본인이 해결하고 싶은 현재 또는 미래 사회의 문제를 찾아서 적어보자.")
        @NotBlank
        String description,
        @Schema(example = "10")
        @NotNull
        Integer score,
        @Schema(example = "1")
        @NotNull
        Integer index,
        List<Option> options// 객관식일 경우만 사용 nullable
    ) {}

    public record Update(
        @NotNull
        Long id,
        @NotNull
        HomeworkQuestionType type,
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        Integer score,
        @NotNull
        Integer index,
        List<Option> options// 객관식일 경우만 사용 nullable
    ) {}

    public record Option(
        @Schema(example = "인공지능")
        @NotBlank
        String content,
        @Schema(example = "1")
        @NotNull
        Integer index,
        @NotNull
        Boolean isAnswer
    ) {}
}
