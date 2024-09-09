package com.abcdedu_backend.survey.entity;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SurveyQuestionType {

    CHOICE("객관식"),
    ESSAY("서술형");

    private final String description;

    public static SurveyQuestionType fromDescription(String description) {
        for (SurveyQuestionType type : SurveyQuestionType.values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        throw new ApplicationException(ErrorCode.SURVEY_TYPE_NOT_FOUND);
    }
}
