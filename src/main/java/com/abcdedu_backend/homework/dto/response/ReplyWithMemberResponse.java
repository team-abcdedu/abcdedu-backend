package com.abcdedu_backend.homework.dto.response;

import com.abcdedu_backend.homework.entity.HomeworkReply;
import com.abcdedu_backend.member.entity.Member;
import lombok.Getter;

/**
 * JPQL 성능 개선을 위해 만든 DTO
 * record로 생성 시, 생성자가 JPQL과 호환되지 않아 에러가 발생하여 POJO 클래스로 생성
 */
@Getter
public class ReplyWithMemberResponse {
    private final Member member;
    private final HomeworkReply homeworkReply;

    public ReplyWithMemberResponse(Member member, HomeworkReply homeworkReply) {
        this.member = member;
        this.homeworkReply = homeworkReply;
    }

}