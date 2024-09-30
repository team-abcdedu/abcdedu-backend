package com.abcdedu_backend.survey.dto.response;

import com.abcdedu_backend.survey.entity.Survey;
import com.abcdedu_backend.survey.entity.SurveyReply;
import lombok.Builder;

import java.util.List;

public class SurveyReplyRes {
    @Builder
    public record Detail(
            String title,
            String description,
            List<SurveyQuestionRes.Detail> questions
    ) {
        public static Detail toDto(Survey survey) {
            return Detail.builder()
                    .title(survey.getTitle())
                    .description(survey.getDescription())
                    .questions(survey.getQuestions().stream()
                            .map(SurveyQuestionRes.Detail::toDto)
                            .toList()
                    )
                    .build();
        }
    }

    @Builder
    public record DetailForRespondent(
            String title,
            String description,
            String orderNumber,
            String questionDescription,
            String answer
    ) {
        public static List<DetailForRespondent> toDto(List<SurveyReply> replies) {
            return replies.stream()
                    .map(reply -> DetailForRespondent.builder()
                            .title(reply.getSurvey().getTitle())
                            .description(reply.getSurvey().getDescription())
                            .orderNumber(reply.getSurveyQuestion().getOrderNumber())
                            .questionDescription(reply.getSurveyQuestion().getContent())
                            .answer(reply.getAnswer())
                            .build())
                          .toList();
        }
    }
}
