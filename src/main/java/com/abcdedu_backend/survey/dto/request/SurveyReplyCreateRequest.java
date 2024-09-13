package com.abcdedu_backend.survey.dto.request;

import com.abcdedu_backend.survey.entity.SurveyQuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record SurveyReplyCreateRequest (
        @Schema(example = "1", description = "400자 이상 넘을 수 없습니다.")
        @NotNull
        @Length(max=400)
        String answer,

        @Schema(example = "CHOICE")
        @NotBlank
        SurveyQuestionType type
){
}
