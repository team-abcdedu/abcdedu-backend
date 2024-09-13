package com.abcdedu_backend.homework.dto;

import com.abcdedu_backend.homework.entity.HomeworkCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class HomeworkReq {
    public record CreateWithQuestion(
        @Schema(example ="사회 문제 해결을 위한 인공지능 설계")
        @NotBlank
        String title,
        @Schema(example = "[인공지능으로 사회문제를 해결한 사례 시청]")
        @NotBlank
        String subTitle,
        @Schema(example = "다양한 사례를 통해 인공지능이 사회문제를 해결하기 위해 어떻게 사용되는지 알아보자. " +
            "그리고 다음 과제를 수행해보자.")
        @NotBlank
        String description,
        @NotNull
        List<HomeworkQuestionReq.Create> questions,
        @NotNull
        String additionalDescription
    ) {
        public HomeworkCommand.Create toCommand() {
            var createQuestionCommands = questions.stream()
                .map(HomeworkQuestionReq.Create::toCommand)
                .toList();
            return HomeworkCommand.Create.builder()
                .title(title)
                .subTitle(subTitle)
                .description(description)
                .additionalDescription(additionalDescription)
                .createQuestionCommands(createQuestionCommands)
                .build();
        }
    }

    public record Update(
        @NotBlank
        String title,
        @NotBlank
        String subTitle,
        @NotBlank
        String description,
        @NotNull
        String additionalDescription
    ) {
        public HomeworkCommand.Update toCommand() {
            return HomeworkCommand.Update.builder()
                .title(title)
                .subTitle(subTitle)
                .description(description)
                .additionalDescription(additionalDescription)
                .build();
        }
    }


}
