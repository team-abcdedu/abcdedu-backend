package com.abcdedu_backend.survey.dto.request;

public record SurveyQuestionCreateRequest(
        String type,
        String content,
        boolean isAnswerRequired
)
{
}
