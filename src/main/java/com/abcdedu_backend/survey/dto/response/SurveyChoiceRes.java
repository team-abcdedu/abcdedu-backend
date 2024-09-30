package com.abcdedu_backend.survey.dto.response;

import com.abcdedu_backend.survey.entity.SurveyQuestionChoice;
import lombok.Builder;

public class SurveyChoiceRes {
    @Builder
    public record Detail(
            Integer orderNumber,
            String description) {

        public static Detail toDto(SurveyQuestionChoice choice) {
            return Detail.builder()
                    .orderNumber(choice.getOrderNumber())
                    .description(choice.getDescription())
                    .build();
        }
    }


}
