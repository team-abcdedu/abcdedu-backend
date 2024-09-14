package com.abcdedu_backend.survey.repository;

import com.abcdedu_backend.survey.entity.Survey;
import com.abcdedu_backend.survey.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
    List<SurveyQuestion> findBySurvey(Survey survey);

}
