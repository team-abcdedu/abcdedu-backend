package com.abcdedu_backend.lecture.repository.v2;

import com.abcdedu_backend.lecture.entity.v2.AssignmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentAnswerRepository extends JpaRepository<AssignmentAnswer, Long> {
}
