package com.abcdedu_backend.survey.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SurveyListResponse (
    Long id,
    String title,
    LocalDateTime createdAt,
    String writerName
){
    public static SurveyListResponse of(Long id, String title, String writerName, LocalDateTime createdAt) {
        return SurveyListResponse.builder()
                .id(id)
                .title(title)
                .writerName(writerName)
                .createdAt(createdAt)
                .build();
    }

}
