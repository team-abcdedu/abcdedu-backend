package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.lecture.entity.SubLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubLectureRepository extends JpaRepository<SubLecture, Long> {
    default SubLecture getById(Long subLectureId) {
        return findById(subLectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.SUB_CLASS_NOT_FOUND));
    }
}
