package com.abcdedu_backend.survey.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SurveyListResponse (
    String title,
    LocalDateTime createAt,
    String writerName
){
}
