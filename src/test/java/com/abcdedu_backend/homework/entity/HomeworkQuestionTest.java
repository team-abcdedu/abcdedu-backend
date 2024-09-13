package com.abcdedu_backend.homework.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeworkQuestionTest {
    @Test
    @DisplayName("과제의 옵션 리스트는 JSON 형식으로 저장되므로, 값이 같은경우 equals() 메서드가 true를 반환해야 한다.")
    void equals() {
        // Given
        HomeworkQuestion.QuestionOption option1 = HomeworkQuestion.QuestionOption.builder()
            .index(1)
            .content("content")
            .build();
        HomeworkQuestion.QuestionOption option2 = HomeworkQuestion.QuestionOption.builder()
            .index(1)
            .content("content")
            .build();

        HomeworkQuestion.QuestionPayload payload1 = HomeworkQuestion.QuestionPayload.builder()
            .questionOptions(List.of(option1))
            .build();

        HomeworkQuestion.QuestionPayload payload2 = HomeworkQuestion.QuestionPayload.builder()
            .questionOptions(List.of(option2))
            .build();

        // When
        boolean result = option1.equals(option2);
        boolean result2 = payload1.equals(payload2);

        // Then
        assertAll(
            () -> assertTrue(result),
            () -> assertTrue(result2)
        );
    }

}