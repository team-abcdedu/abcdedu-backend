package com.abcdedu_backend.survey.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record SurveyGetResponse (
        String title,
        String description,
        String writerName,
        List<SurveyQuestionGetResponse> questionGetResponses
){
}
