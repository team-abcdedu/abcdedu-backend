package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkReplyRepository extends JpaRepository<HomeworkReply, Long> {
    List<HomeworkReply> findAllByHomework(Homework homework);
}
