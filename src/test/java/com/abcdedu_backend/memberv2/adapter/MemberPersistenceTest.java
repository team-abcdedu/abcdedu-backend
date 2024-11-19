package com.abcdedu_backend.memberv2.adapter;

import com.abcdedu_backend.memberv2.adapter.in.dto.request.MemberSearchCondition;
import com.abcdedu_backend.memberv2.adapter.out.MemberJpaRepository;
import com.abcdedu_backend.memberv2.adapter.out.MemberPersistence;
import com.abcdedu_backend.memberv2.application.domain.Member;
import com.abcdedu_backend.memberv2.application.domain.MemberRole;
import com.abcdedu_backend.post.entity.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MemberPersistenceTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    private MemberPersistence memberRepository;

    @BeforeEach
    void beforeEachSetup() {
        memberRepository=new MemberPersistence(memberJpaRepository);
    }

//    @AfterEach
//    void clear() {
//        memberJpaRepository.deleteAllInBatch();
//    }

    void setUp() {
        Member member1 = new Member(null, "basic1", "basic1@example.com", "password1004", "Basic School", 1001L, "key1", MemberRole.BASIC, null, null, null);
        Member member2 = new Member(null, "basic2", "basic2@example.com", "password1004", "Basic School", 1002L, "key2", MemberRole.BASIC, null, null, null);
        Member member3 = new Member(null, "admin", "admin@example.com", "password1004", "Admin School", 1003L, "key3", MemberRole.ADMIN, null, null, null);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }

    Page<Member> findMemberWithPageAndCond(int pageNumber, int pageSize, MemberSearchCondition cond) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Member> result = memberRepository.findAllByCondition(cond.school(), cond.studentId(), cond.name(), cond.role(), pageable);
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
    public void Member저장(){
        final Member member = createBasicMember();

        Member result = memberRepository.save(member);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("고동천");
        assertThat(result.getEmail()).isEqualTo("ehdcjs159@gmail.com");
        assertThat(result.getRole()).isEqualTo(MemberRole.BASIC);
        assertThat(result.getSchool()).isNull();
        assertThat(result.getImageObjectKey()).isNull();
        assertThat(result.getStudentId()).isNull();
    }

    @Test
    public void Member가존재하는지() {
        final Member member = createBasicMember();
        memberRepository.save(member);

        final Member findMember = memberRepository.findByEmail("ehdcjs159@gmail.com").get();

        assertThat(findMember.getId()).isNotNull();
        assertThat(findMember.getName()).isEqualTo("고동천");
        assertThat(findMember.getEmail()).isEqualTo("ehdcjs159@gmail.com");
        assertThat(findMember.getRole()).isNotNull();
    }

    @Test
    public void Email_중복_생성시_오류() {
        final Member member1 = createBasicMember();
        final Member member2 = createBasicMember();
        memberRepository.save(member1);

        Assertions.assertThrows(DataAccessException.class, () -> memberRepository.save(member2));
    }

    private Member createBasicMember() {
        return Member.builder()
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword("abcd1234!!")
                .role(MemberRole.BASIC)
                .build();
    }

}