package com.abcdedu_backend.homework.dto.response;

import lombok.Builder;

@Builder
public record HomeworkQuestionGetRes(
        String orderNumber, // 보여질 문제 순서
        String content, // 문제 내용
        boolean isAnswerRequired // 필수 답변 여부

) {
}
