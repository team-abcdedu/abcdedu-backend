package com.abcdedu_backend.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "survey_questions")
public class SurveyQuestion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String orderNumber; // 순서대로 출력하기 위한 용도 아님, 서브 질문 처리용

    @Column(nullable = false, length = 100)
    private String content; // 질문 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurveyQuestionType type;

    @Column(nullable = false)
    private boolean isAnswerRequired; // 답변 필수인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
}
