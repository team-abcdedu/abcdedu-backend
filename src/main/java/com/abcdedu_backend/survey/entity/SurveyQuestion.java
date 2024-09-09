package com.abcdedu_backend.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(nullable = false, length = 100)
    private String content; // 질문 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurveyQuestionType type;

    @Column(nullable = false)
    private boolean isAnswerRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "surveyQuestion", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<SurveyQuestionChoice> choices;

    public void updateSurveyQuestion(String content, boolean isAnswerRequired) {
        this.content = content;
        this.isAnswerRequired = isAnswerRequired;
    }
}
