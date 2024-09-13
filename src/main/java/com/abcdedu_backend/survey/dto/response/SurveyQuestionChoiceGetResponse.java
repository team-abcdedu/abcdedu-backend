package com.abcdedu_backend.survey.dto.response;

import lombok.Builder;

@Builder
public record SurveyQuestionChoiceGetResponse(
        Integer orderNumber,
        String description
) {
}
