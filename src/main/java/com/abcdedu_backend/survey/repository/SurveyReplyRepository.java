package com.abcdedu_backend.survey.repository;

import com.abcdedu_backend.survey.entity.SurveyReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyReplyRepository extends JpaRepository<SurveyReply, Long> {

    Optional<SurveyReply> findBySurveyId(Long surveyId);
}
