package com.abcdedu_backend.homework.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class HomeworkQuestionCommand {
    @Builder
    @Getter
    public static class Create{
        @NotNull
        private final HomeworkQuestionType type;
        @NotBlank
        private final String title;
        @NotBlank
        private final String description;
        @Min(0)
        private final Integer score;
        @Min(0)
        private final Integer index;
        private final List<CreateOption> createOptionsCommand;
    }

    @Builder
    @Getter
    public static class CreateOption{
        @NotBlank
        private final String content;
        @Min(0)
        private final Integer index;
    }
}
