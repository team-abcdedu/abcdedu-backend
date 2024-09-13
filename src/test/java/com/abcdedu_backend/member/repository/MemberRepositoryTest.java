package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void Member저장(){
        final Member member = createBasicMember();

        final Member result = memberRepository.save(member);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("고동천");
        assertThat(result.getEmail()).isEqualTo("ehdcjs159@gmail.com");
        assertThat(result.getRole()).isEqualTo(MemberRole.BASIC);
        assertThat(result.getEncodedPassword()).isEqualTo("abcd1234!!");
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
        assertThat(findMember.getEncodedPassword()).isEqualTo("abcd1234!!");
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