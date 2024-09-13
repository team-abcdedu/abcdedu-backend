package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
}
