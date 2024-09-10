package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.lecture.entity.AssignmentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, Long> {
}
