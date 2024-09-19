package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkQuestionRepository extends JpaRepository<HomeworkQuestion, Long> {
    List<HomeworkQuestion> findAllByHomework(Homework homework);
}
