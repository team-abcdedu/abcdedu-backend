package com.abcdedu_backend.survey.repository;

import com.abcdedu_backend.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Page<Survey> findAll(Pageable pageable);

}
