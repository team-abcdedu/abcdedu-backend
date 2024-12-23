package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    Page<Homework> findAll(Pageable pageable);
}
