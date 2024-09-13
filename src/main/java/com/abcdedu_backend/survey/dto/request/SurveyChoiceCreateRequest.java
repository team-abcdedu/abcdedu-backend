package com.abcdedu_backend.survey.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SurveyChoiceCreateRequest (
        @Schema(example = "1")
        @NotNull
        Integer orderNumber,

        @Schema(example = "A-1 학생들을 위한 머신러닝")
        @NotNull
        String description
){

}
