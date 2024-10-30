package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.dto.request.MemberSearchCondition;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AdminMemberRepositoryTest {

    @Autowired
    private AdminMemberRepository adminMemberRepository;
    @Autowired
    private MemberRepository memberRepository;

    void setUp() {
        Member member1 = new Member(1L, "basic1", "basic1@example.com", "password1004", "Basic School", 1001L, "key1", MemberRole.BASIC, null, null);
        Member member2 = new Member(2L, "basic2", "basic2@example.com", "password1004", "Basic School", 1002L, "key2", MemberRole.BASIC, null, null);
        Member member3 = new Member(3L, "admin", "admin@example.com", "password1004", "Admin School", 1003L, "key3", MemberRole.ADMIN, null, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    Page<Member> findMemberWithPageAndCond(int pageNumber, int pageSize, MemberSearchCondition cond) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Member> result = adminMemberRepository.findAllByCondition(cond.school(), cond.studentId(), cond.name(), cond.role(), pageable);
        return result;
    }

    @Test
    void 학교와_이름으로_멤버_조회_성공() {
        setUp();

        MemberSearchCondition cond = new MemberSearchCondition("Basic School", null, "basic1", null);
        Page<Member> result = findMemberWithPageAndCond(0, 10, cond);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("basic1");
    }

    @Test
    void 아무_조건_없이_조회하면_모든_멤버_조회_성공() {
        setUp();

        MemberSearchCondition cond = new MemberSearchCondition(null, null, null, null);
        Page<Member> result = findMemberWithPageAndCond(0, 10, cond);

        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void 역할에_따른_멤버_조회_성공() {
        setUp();

        MemberSearchCondition cond = new MemberSearchCondition(null, null, null, MemberRole.BASIC);
        Page<Member> result = findMemberWithPageAndCond(0, 10, cond);

        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void 학교명과_이름은_글자_일부만_검색해도_조회_성공() {
        setUp();

        MemberSearchCondition cond = new MemberSearchCondition("B", null, "sic1", null);
        Page<Member> result = findMemberWithPageAndCond(0, 10, cond);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("basic1");
        assertThat(result.getContent().get(0).getSchool()).isEqualTo("Basic School");
    }

    @Test
    void findAllByIdIn_조건에_맞는_멤버_조회_성공() {
        setUp();

        List<Member> result = adminMemberRepository.findAllByIdIn(List.of(1L, 2L));

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void findAllByIdIn_조건에_맞는_멤버_없으면_빈값이_반환() {
        setUp();

        List<Member> result = adminMemberRepository.findAllByIdIn(List.of(4L, 5L));

        assertThat(result.size()).isEqualTo(0);
    }


}