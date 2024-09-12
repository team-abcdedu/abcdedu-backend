package com.abcdedu_backend.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class HomeworkReq {
    public record CreateWithQuestion(
        @Schema(example ="사회 문제 해결을 위한 인공지능 설계")
        @NotBlank
        String title,
        @Schema(example = """
            <h2>[인공지능으로 사회문제를 해결한 사례 시청]</h2>
            <p>다양한 사례를 통해 인공지능이 사회문제를 해결하기 위해 어떻게 사용되는지 알아보자.
            그리고 다음 과제를 수행해보자.</p>""")
        @NotBlank
        String description,
        @NotNull
        List<HomeworkQuestionReq.Create> questions,
        @Schema(example = """
            <h3>[토론 및 발표]</h3>
            <p>인공지능을 활용하여 사회문제를 해결하기 위한 방법을 발표하고 토론해보자.</p>
            """)
        @NotNull
        String additionalDescription
    ) {}

    public record Update(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        String additionalDescription
    ) {}


}
