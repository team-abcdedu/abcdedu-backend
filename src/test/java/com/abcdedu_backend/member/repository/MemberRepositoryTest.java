package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void Member저장(){
        final Member member = Member.builder()
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword("abcd1234!!")
                .role(MemberRole.BASIC)
                .build();

        final Member result = memberRepository.save(member);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("고동천");
        assertThat(result.getEmail()).isEqualTo("ehdcjs159@gmail.com");
        assertThat(result.getRole()).isEqualTo(MemberRole.BASIC);
        assertThat(result.getEncodedPassword()).isEqualTo("abcd1234!!");
        assertThat(result.getSchool()).isNull();
        assertThat(result.getImageUrl()).isNull();
        assertThat(result.getStudentId()).isNull();
    }

    @Test
    public void Member가존재하는지() {
        final Member member = Member.builder()
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword("abcd1234!!")
                .role(MemberRole.BASIC)
                .build();
        final Member result = memberRepository.save(member);

        final Member findMember = memberRepository.findByEmail("ehdcjs159@gmail.com").get();

        assertThat(findMember.getId()).isNotNull();
        assertThat(findMember.getName()).isEqualTo("고동천");
        assertThat(findMember.getEmail()).isEqualTo("ehdcjs159@gmail.com");
        assertThat(findMember.getEncodedPassword()).isEqualTo("abcd1234!!");
        assertThat(findMember.getRole()).isNotNull();
    }

}