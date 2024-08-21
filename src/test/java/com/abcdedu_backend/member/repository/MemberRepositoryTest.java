package com.abcdedu_backend.member.repository;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void Member저장(){
        final Member member = Member.builder()
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword("abcd1234!!")
                .build();

        final Member result = memberRepository.save(member);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("고동천");
        assertThat(result.getEmail()).isEqualTo("ehdcjs159@gmail.com");
        assertThat(result.getRole()).isEqualTo(MemberRole.BASIC);
        assertThat(result.getEncodedPassword()).isEqualTo("abcd1234!!");
        assertThat(result.getSchool()).isNull();
        assertThat(result.getImage_url()).isNull();
        assertThat(result.getStudent_id()).isNull();
    }

    @Test
    public void Member가존재하는지() {
        final Member member = Member.builder()
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword("abcd1234!!")
                .build();
        final Member result = memberRepository.save(member);

        final Member findMember = memberRepository.findByEmail("ehdcjs159@gmail.com");

        assertThat(findMember.getId()).isNotNull();
        assertThat(findMember.getName()).isEqualTo("고동천");
        assertThat(findMember.getEmail()).isEqualTo("ehdcjs159@gmail.com");
        assertThat(findMember.getEncodedPassword()).isEqualTo("abcd1234!!");
        assertThat(findMember.getRole()).isNotNull();
    }

}