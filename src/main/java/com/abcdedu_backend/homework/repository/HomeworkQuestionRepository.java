package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.HomeworkQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkQuestionRepository extends JpaRepository<HomeworkQuestion, Long> {
}
