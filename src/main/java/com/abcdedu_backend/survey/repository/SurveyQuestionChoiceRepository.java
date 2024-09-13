package com.abcdedu_backend.survey.repository;

import com.abcdedu_backend.survey.entity.SurveyQuestion;
import com.abcdedu_backend.survey.entity.SurveyQuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyQuestionChoiceRepository extends JpaRepository<SurveyQuestionChoice, Long> {
    List<SurveyQuestionChoice> findBysurveyQuestion(SurveyQuestion question);

}
