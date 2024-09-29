package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.lecture.entity.AssignmentAnswerFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentAnswerFileRepository extends JpaRepository<AssignmentAnswerFile, Long> {

    default AssignmentAnswerFile getById(Long assignmentAnswerFileId) {
        return findById(assignmentAnswerFileId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_ANSWER_FILE_NOT_FOUND));
    }
}
