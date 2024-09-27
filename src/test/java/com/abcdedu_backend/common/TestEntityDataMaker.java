package com.abcdedu_backend.common;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.survey.entity.Survey;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestEntityDataMaker {
    public static Survey surveyWithMember(Member member) {
        return Survey.builder()
                .title("테스트 설문 제목")
                .description("테스트 설문 내용")
                .additionalDescription("테스트 추가 내용")
                .member(member)
                .build();
    }

    public static List<Survey> createFiveSurvey(Member member) {
        List<Survey> surveys = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            surveys.add(Survey.builder()
                    .title("테스트 설문 제목")
                    .description("테스트 설문 내용")
                    .member(member)
                    .build());
        }
        log.info("설문 생성 완료, 리스트 사이즈 : {}", surveys.size());
        return surveys;
    }

    public static Member memberBeGivenRoleAndMeetNullCondition(MemberRole memberRole) {
        return Member.builder()
                .email("test@gamil.com")
                .role(memberRole)
                .name("테스트 유저")
                .encodedPassword("isRequiredLengthOverThan7")
                .build();
    }
}
