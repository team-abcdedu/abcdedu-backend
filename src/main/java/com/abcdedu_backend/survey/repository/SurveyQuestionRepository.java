package com.abcdedu_backend.survey.repository;

import com.abcdedu_backend.survey.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
}
