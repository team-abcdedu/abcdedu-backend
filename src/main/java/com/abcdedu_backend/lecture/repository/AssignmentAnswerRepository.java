package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.lecture.entity.AssignmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentAnswerRepository extends JpaRepository<AssignmentAnswer, Long> {
}
