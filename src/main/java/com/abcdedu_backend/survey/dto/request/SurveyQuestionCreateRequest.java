package com.abcdedu_backend.survey.dto.request;

import com.abcdedu_backend.survey.entity.SurveyQuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SurveyQuestionCreateRequest(

        @Schema(allowableValues = {"CHOICE", "ESSAY"}, example = "CHOICE")
        @NotNull
        SurveyQuestionType type,

        @Schema(example = "1", description = "문제 번호. 순서 정렬용이 아닌 서브 설문 표현용")
        String orderNumber,

        @Schema(example = "본인이 수강한 수업을 선택해주세요")
        @NotNull
        String content,
        @Schema(example = "true")
        boolean isAnswerRequired,

        @Schema(description = "서술형 질문일 때에는, [] 와 같이 빈 리스트를 넣어주세요")
        List<@Valid SurveyChoiceCreateRequest> choices // 객관식에만 사용
)
{

}
