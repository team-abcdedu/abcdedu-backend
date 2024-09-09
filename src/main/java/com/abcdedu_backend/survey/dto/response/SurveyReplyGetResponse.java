package com.abcdedu_backend.survey.dto.response;

import lombok.Builder;

@Builder
public record SurveyReplyGetResponse (
        Long surveyId,
        String surveyTitle,
        Long questionId,
        String questionDescription,
        Long surveyReplyId,
        String answer,
        String replyedMemberName

){

}
