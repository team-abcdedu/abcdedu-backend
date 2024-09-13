package com.abcdedu_backend.survey.dto.response;

import com.abcdedu_backend.survey.entity.SurveyQuestionType;
import lombok.Builder;

import java.util.List;

@Builder
public record SurveyQuestionGetResponse (
        SurveyQuestionType type,
        String orderNumber,
        boolean isAnswerRequired,
        String content,
        List<SurveyQuestionChoiceGetResponse> choices

){
}
