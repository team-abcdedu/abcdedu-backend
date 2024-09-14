package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.HomeworkReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeworkReplyRepository extends JpaRepository<HomeworkReply, Long> {
    @Query("SELECT hr FROM HomeworkReply hr " +
        "WHERE hr.member.id = :memberId AND hr.homeworkQuestion IN :homeworkQuestions")
    List<HomeworkReply> findAllByMemberIdAndQuestionIdsIn(
        @Param("memberId") Long memberId,
        @Param("homeworkQuestions") List<Long> homeworkQuestionsIds
    );
}
