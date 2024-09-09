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
@Table(name = "survey_question_choices")
public class SurveyQuestionChoice {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name  = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name  = "survey_question_id", nullable = false)
    private SurveyQuestion surveyQuestion;

    @Column(nullable = false)
    private Integer order; // 선택지 순서

    @Column(nullable = false, length = 30)
    private String description; // 해당 선택지가 나타내는 의미, ex) "매우 좋음"
}
