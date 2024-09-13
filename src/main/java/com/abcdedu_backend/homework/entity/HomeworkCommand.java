package com.abcdedu_backend.homework.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class HomeworkCommand {
    @Builder
    @Getter
    public static class Create{
        @NotBlank
        private final String title;
        @NotBlank
        private final String description;
        @NotBlank
        private final String subTitle;
        @NotNull
        private final List<HomeworkQuestionCommand.Create> createQuestionCommands;
        @NotNull
        private final String additionalDescription;
    }

    @Builder
    @Getter
    public static class Update{
        @NotBlank
        private final String title;
        @NotBlank
        private final String subTitle;
        @NotBlank
        private final String description;
        @NotNull
        private final String additionalDescription;
    }
}
