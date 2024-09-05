package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.lecture.entity.AssignmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentQuestionRepository extends JpaRepository<AssignmentQuestion, Long> {
}
