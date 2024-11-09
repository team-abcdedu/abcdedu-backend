package com.abcdedu_backend.homework.entity;

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
@Table(name = "homework_questions")
public class HomeworkQuestion  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String orderNumber; // 순서대로 출력하기 위한 용도 아님, 서브 질문 처리용

    @Column(nullable = false, length = 500)
    private String content; // 질문 내용

    @Column(length = 500)
    private String additionalContent; // 추가 질문 내용

    @Column(nullable = false)
    private boolean isAnswerRequired; // 답변 필수인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homework_id", nullable = false)
    private Homework homework;
}
