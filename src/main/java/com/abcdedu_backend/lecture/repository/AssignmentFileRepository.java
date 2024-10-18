package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.lecture.entity.AssignmentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, Long> {

    default AssignmentFile getById(Long assignmentFileId) {
        return findById(assignmentFileId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_FILE_NOT_FOUND));
    }
}
