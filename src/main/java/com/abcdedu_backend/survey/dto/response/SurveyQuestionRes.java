package com.abcdedu_backend.survey.dto.response;

import com.abcdedu_backend.survey.entity.SurveyQuestion;
import com.abcdedu_backend.survey.entity.SurveyQuestionType;
import lombok.Builder;

import java.util.List;


public class SurveyQuestionRes {
    @Builder
    public record Detail(
            SurveyQuestionType type,
            String orderNumber,
            boolean isAnswerRequired,
            String content,
            List<SurveyChoiceRes.Detail> choices
    ) {
        public static Detail toDto(SurveyQuestion question) {
            return Detail.builder()
                    .type(question.getType())
                    .orderNumber(question.getOrderNumber())
                    .isAnswerRequired(question.isAnswerRequired())
                    .content(question.getContent())
                    .choices(question.getChoices().stream()
                            .map(SurveyChoiceRes.Detail::toDto)
                            .toList()
                    )
                    .build();
        }

    }
}
