package com.abcdedu_backend.survey.dto.response;

import com.abcdedu_backend.survey.entity.Survey;
import lombok.Builder;

import java.time.LocalDateTime;

public class SurveyRes {
    @Builder
    public record Detail(
            String title,
            String description,
            String writerName,
            LocalDateTime createdAt
    ) {
        public static Detail to(Survey survey) {
            return Detail.builder()
                    .title(survey.getTitle())
                    .description(survey.getDescription())
                    .writerName(survey.getWriter().getName())
                    .createdAt(survey.getCreatedAt())
                    .build();
        }
    }

    @Builder
    public record List(
            Long id,
            String title,
            LocalDateTime createdAt,
            String writerName
    ) {
        public static List to(Survey survey) {
            return SurveyRes.List.builder()
                    .id(survey.getId())
                    .title(survey.getTitle())
                    .writerName(survey.getWriter().getName())
                    .createdAt(survey.getCreatedAt())
                    .build();
        }
    }


}
