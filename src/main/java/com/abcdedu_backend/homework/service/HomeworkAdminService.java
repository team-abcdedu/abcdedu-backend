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
    private final ReadRepresentativeService representativeService;

    public Page<HomeworkRes> getHomeworks(Pageable pageable) {
        Long representativeHomeworkId = representativeService.readRepresentativeHomework();

        Page<Homework> homeworks = homeworkRepository.findAll(pageable);
        return homeworks.map(homework ->
                HomeworkRes.fromHomework(
                        homework,
                        homework.getId().equals(representativeHomeworkId)
                )
        );
    }

    @Transactional
    public void registerAsRepresentative(Long registrantId, RepresentativeRegisterRequest request) {
        Homework homework = checkHomework(request.homeworkId());
        Member member = checkMember(registrantId);
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
