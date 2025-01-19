package com.abcdedu_backend.homework.repository;
import com.abcdedu_backend.homework.dto.response.ReplyWithMemberResponse;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HomeworkReplyRepository extends JpaRepository<HomeworkReply, Long> {
    /**
     * 특정 과제에 대한 응답 조회 (생성 날짜 범위 필터링)
     *
     * @param homework 과제
     * @param fromDate 시작 날짜 (nullable)
     * @param toDate   종료 날짜 (nullable)
     * @return 필터링된 응답 목록
     */
    @Query("SELECT new com.abcdedu_backend.homework.dto.response.ReplyWithMemberResponse(m, hr) " +
            "FROM HomeworkReply hr " +
            "JOIN hr.member m " +
            "WHERE hr.createdAt IN (" +
            "  SELECT MAX(hr2.createdAt) FROM HomeworkReply hr2 " +
            "  WHERE hr2.homework = :homework " +
            "    AND (:fromDate IS NULL OR hr2.createdAt >= :fromDate) " +
            "    AND (:toDate IS NULL OR hr2.createdAt <= :toDate) " +
            "  GROUP BY hr2.member, hr2.homeworkQuestion" +
            ")")
    List<ReplyWithMemberResponse> findRepliesByHomeworkAndCreatedAtBetween(
            @Param("homework") Homework homework,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
}
