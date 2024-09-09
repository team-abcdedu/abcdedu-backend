package com.abcdedu_backend.survey.dto.response;

import lombok.Builder;

@Builder
public record SurveyQuestionGetResponse (
        String type,
        boolean isAnswerRequired,
        String content

){
}
