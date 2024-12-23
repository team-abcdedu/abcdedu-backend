package com.abcdedu_backend.homework.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.homework.dto.request.RepresentativeRegisterRequest;
import com.abcdedu_backend.homework.dto.response.HomeworkRes;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.entity.HomeworkRepresentative;
import com.abcdedu_backend.homework.repository.HomeworkRepository;
import com.abcdedu_backend.homework.repository.HomeworkRepresentativeRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkAdminService {
    private final HomeworkRepository homeworkRepository;
    private final HomeworkRepresentativeRepository representativeRepository;
    private final MemberRepository memberRepository;

    public Page<HomeworkRes> getHomeworks(Pageable pageable) {
        Page<Homework> homeworks = homeworkRepository.findAll(pageable);
        log.info("homework repository에서 Page로 데이터 불러오기 성공");
        return homeworks.map(HomeworkRes::fromHomework);
    }

    @Transactional
    public void registerAsRepresentative(RepresentativeRegisterRequest request) {
        Homework homework = checkHomework(request.homeworkId());
        Member member = checkMember(request.memberId());
        try {
            representativeRepository.save(HomeworkRepresentative.of(homework, member));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.DATABASE_ERROR);
        }
    }

    private Homework checkHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.HOMEWORK_NOT_FOUND));
        return homework;
    }

    private Member checkMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        return member;
    }

}
