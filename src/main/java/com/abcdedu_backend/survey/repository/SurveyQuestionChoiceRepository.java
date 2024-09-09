package com.abcdedu_backend.survey.repository;

import com.abcdedu_backend.survey.entity.SurveyQuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyQuestionChoiceRepository extends JpaRepository<SurveyQuestionChoice, Long> {
    List<SurveyQuestionChoice> findBySurveyIdAndSurveyQuestionId(Long surveyId, Long questionId);
    SurveyQuestionChoice findBySurveyIdAndSurveyQuestionIdAndOrder(Long surveyId, Long questionId, Integer order);
}
