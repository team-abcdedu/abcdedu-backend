package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.HomeworkReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeworkReplyRepository extends JpaRepository<HomeworkReply, Long> {
    @Query("SELECT hr FROM HomeworkReply hr " +
        "JOIN HomeworkQuestion hq ON hr.homeworkQuestion.id = hq.id " +
        "WHERE hr.member.id = :memberId AND hq.homework.id = :homeworkId")
    List<HomeworkReply> findAllByMemberIdAndHomeworkId(
        @Param("memberId") Long memberId,
        @Param("homeworkId") Long homeworkId
    );
}
