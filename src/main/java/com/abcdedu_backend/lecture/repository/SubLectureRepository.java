package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.lecture.entity.SubLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubLectureRepository extends JpaRepository<SubLecture, Long> {
}
