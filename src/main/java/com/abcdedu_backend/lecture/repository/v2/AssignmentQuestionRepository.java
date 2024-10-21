package com.abcdedu_backend.lecture.repository.v2;

import com.abcdedu_backend.lecture.entity.v2.AssignmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentQuestionRepository extends JpaRepository<AssignmentQuestion, Long> {
}
