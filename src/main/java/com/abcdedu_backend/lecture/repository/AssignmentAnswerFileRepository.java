package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.lecture.entity.AssignmentAnswerFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentAnswerFileRepository extends JpaRepository<AssignmentAnswerFile, Long> {
}
