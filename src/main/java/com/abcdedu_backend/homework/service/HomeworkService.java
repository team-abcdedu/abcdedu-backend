package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkCommand;
import com.abcdedu_backend.homework.repository.HomeworkRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final MemberRepository memberRepository;
    private final HomeworkRepository homeworkRepository;

    @Transactional
    public Long createHomework(Long memberId, HomeworkCommand.Create command) {
        Member member = memberRepository.getReferenceById(memberId);
        Homework homework = Homework.create(command, member);
        homeworkRepository.save(homework);
        return homework.getId();
    }

    @Transactional
    public void updateHomework(
        Long teacherId,
        HomeworkCommand.Update command,
        Long homeworkId
    ) {
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow();
        if(!homework.getTeacherId().equals(teacherId)) {
            throw new IllegalArgumentException("Teacher does not have permission to update homework");
        }
        homework.update(command);
    }
}
