package com.abcdedu_backend.homework.repository;

import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkRepresentative;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class HomeworkRepresentativeRepositoryTest {
    @Autowired
    private HomeworkRepresentativeRepository homeworkRepresentativeRepository;

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findLatestRepresentative_함수는_마지막으로_대표_과제로_지정된_과제가_반환한다() {
        // Given
        Member member = memberRepository.save(createAdminMember());
        Homework homework1 = homeworkRepository.save(createHomework(1,member));
        Homework homework2 = homeworkRepository.save(createHomework(2,member));

        homeworkRepresentativeRepository.save(HomeworkRepresentative.of(homework1, member));
        HomeworkRepresentative latestRepresentative = homeworkRepresentativeRepository.save(HomeworkRepresentative.of(homework2, member)
        );

        // When
        Optional<HomeworkRepresentative> result = homeworkRepresentativeRepository.findLatestRepresentative();

        // Then
        assertTrue(result.isPresent());
        assertEquals(latestRepresentative.getId(), result.get().getId());
    }

    private Member createAdminMember() {
        return Member.builder()
                .name("관리자")
                .email("abcdedu@gmail.com")
                .encodedPassword("abcd1234!!")
                .role(MemberRole.ADMIN)
                .build();
    }

    private Homework createHomework(int num, Member creater) {
        return Homework.builder()
                .title("과제 제목" + num)
                .description("과제 설명입니다.")
                .additionalDescription("과제 부연 설명입니다.")
                .member(creater)
                .build();
    }
}