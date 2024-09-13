package com.abcdedu_backend.survey.repository;

import com.abcdedu_backend.survey.entity.Survey;
import com.abcdedu_backend.survey.entity.SurveyReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyReplyRepository extends JpaRepository<SurveyReply, Long> {

    List<SurveyReply> findBySurvey(Survey survey);
}
