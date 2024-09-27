package com.abcdedu_backend.survey.repository;

import com.abcdedu_backend.common.TestEntityDataMaker;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.MemberRepository;
import com.abcdedu_backend.survey.entity.Survey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest // JpaRepository 클래스를 의존받을 수 있게한다.
@ActiveProfiles("test")
@DisplayName("survey 엔티티에 대한 repository Test")
class SurveyRepositoryTest {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void deleteAllData() {
        surveyRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void 설문_생성_성공() {
        //given
        Member member = memberRepository.save(TestEntityDataMaker.memberBeGivenRoleAndMeetNullCondition(MemberRole.ADMIN));
        Survey survey = TestEntityDataMaker.surveyWithMember(member);
        //when
        Survey savedSurvey = surveyRepository.save(survey);
        //then
        assertThat(savedSurvey.getId()).isEqualTo(survey.getId());
    }

    @Test
    void 설문_ID별_조회_성공() {
        //given
        Member member = memberRepository.save(TestEntityDataMaker.memberBeGivenRoleAndMeetNullCondition(MemberRole.ADMIN));
        Survey survey = surveyRepository.save(TestEntityDataMaker.surveyWithMember(member));
        //when
        Survey byId = surveyRepository.findById(survey.getId()).get();
        //then
        assertThat(byId.getTitle()).isEqualTo("테스트 설문 제목");
    }

    @Test
    void 설문_목록_조회_성공() {
        //given
        Member member = memberRepository.save(TestEntityDataMaker.memberBeGivenRoleAndMeetNullCondition(MemberRole.ADMIN));
        List<Survey> fiveSurvey = TestEntityDataMaker.createFiveSurvey(member);
        surveyRepository.saveAll(fiveSurvey);
        //when
        List<Survey> all = surveyRepository.findAll();
        //then
        assertThat(all.size()).isEqualTo(5);
    }

    @Test
    void 설문_수정_성공() {
        //given
        Member member = memberRepository.save(TestEntityDataMaker.memberBeGivenRoleAndMeetNullCondition(MemberRole.ADMIN));
        Survey survey = surveyRepository.save(TestEntityDataMaker.surveyWithMember(member));
        //when
        survey.update("제목을 수정합니다.", "설명을 수정합니다.", "추가 설명을 수정합니다.");
        Survey updatedSurvey = surveyRepository.findById(survey.getId()).get();
        //then
        assertThat(updatedSurvey.getTitle()).isEqualTo("제목을 수정합니다.");
        assertThat(updatedSurvey.getDescription()).isEqualTo("설명을 수정합니다.");
    }

}
