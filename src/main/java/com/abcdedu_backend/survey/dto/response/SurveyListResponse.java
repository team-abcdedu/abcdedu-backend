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
}
