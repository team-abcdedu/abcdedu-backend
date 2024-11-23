package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkReply;
import com.abcdedu_backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeworkReplyRepository extends JpaRepository<HomeworkReply, Long> {
    List<HomeworkReply> findByHomeworkAndMember(Homework homework, Member member);

    @Query("SELECT r FROM HomeworkReply r " +
                  "JOIN FETCH r.member m " +
                  "JOIN FETCH r.homeworkQuestion q " +
                  "WHERE r.homework.id = :homeworkId")
    List<HomeworkReply> findRepliesByHomeworkId(@Param("homeworkId") Long homeworkId);
}
