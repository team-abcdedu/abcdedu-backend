package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.lecture.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
