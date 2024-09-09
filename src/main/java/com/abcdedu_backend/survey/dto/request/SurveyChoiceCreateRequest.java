package com.abcdedu_backend.survey.dto.request;

public record SurveyChoiceCreateRequest (
        Integer order,
        String description
){
}
