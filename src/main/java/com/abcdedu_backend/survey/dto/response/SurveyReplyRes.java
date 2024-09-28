package com.abcdedu_backend.survey.dto.response;

import lombok.Builder;

import java.util.List;

public class SurveyReplyRes {
    @Builder
    public record Detail(
            String title,
            String description,
            String writerName,
            List<SurveyQuestionRes.getDetail> questions
    ) {

    }
}
