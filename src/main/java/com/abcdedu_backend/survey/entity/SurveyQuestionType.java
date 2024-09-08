package com.abcdedu_backend.survey.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SurveyQuestionType {

    CHOICE("객관식"),
    ESSAY("서술형");

    private final String description;
}
