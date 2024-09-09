package com.abcdedu_backend.survey.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SurveyCreateRequest (
        @NotBlank
        String title,

        String description
){

}
