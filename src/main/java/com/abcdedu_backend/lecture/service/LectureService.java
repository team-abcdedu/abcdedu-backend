package com.abcdedu_backend.lecture.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.lecture.dto.CreateLectureRequest;
import com.abcdedu_backend.lecture.entity.Lecture;
import com.abcdedu_backend.lecture.repository.LectureRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    public void createLecture(Long memberId, CreateLectureRequest request) {
        Member findMember = findMember(memberId);
        checkPermission(findMember);
        Lecture lecture = createLecture(request);
        lectureRepository.save(lecture);
    }

    private Lecture createLecture(CreateLectureRequest request) {
        return Lecture.builder()
                .title(request.title())
                .type(request.type())
                .description(request.description())
                .build();
    }

    private Member findMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        return findMember;
    }

    private void checkPermission(Member findMember) {
        if (findMember.getRole() != MemberRole.ADMIN){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
