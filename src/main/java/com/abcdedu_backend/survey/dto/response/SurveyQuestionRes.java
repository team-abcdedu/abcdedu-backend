package com.abcdedu_backend.survey.dto.response;

import com.abcdedu_backend.survey.entity.SurveyQuestionType;
import lombok.Builder;

import java.util.List;

@Builder
public class SurveyQuestionRes {
    public record getDetail(
            SurveyQuestionType type,
            String orderNumber,
            boolean isAnswerRequired,
            String content,
            List<SurveyChoiceRes.getDetail> choices
    ) {}
}
