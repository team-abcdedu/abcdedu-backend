package com.abcdedu_backend.lecture.repository;

import com.abcdedu_backend.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
}
