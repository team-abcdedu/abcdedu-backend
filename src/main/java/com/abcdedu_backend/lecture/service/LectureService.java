package com.abcdedu_backend.lecture.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.lecture.dto.CreateAssignmentQuestionsRequest;
import com.abcdedu_backend.lecture.dto.CreateAssignmentRequest;
import com.abcdedu_backend.lecture.dto.CreateLectureRequest;
import com.abcdedu_backend.lecture.dto.CreateSubLectureRequest;
import com.abcdedu_backend.lecture.entity.*;
import com.abcdedu_backend.lecture.repository.AssignmentQuestionRepository;
import com.abcdedu_backend.lecture.repository.AssignmentRepository;
import com.abcdedu_backend.lecture.repository.LectureRepository;
import com.abcdedu_backend.lecture.repository.SubLectureRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final SubLectureRepository subLectureRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentQuestionRepository assignmentQuestionRepository;

    @Transactional
    public void createLecture(Long memberId, CreateLectureRequest request) {
        Member findMember = findMember(memberId);
        checkPermission(findMember);
        Lecture lecture = createLecture(request);
        lectureRepository.save(lecture);
    }

    @Transactional
    public void createSubLecture(Long lectureId, Long memberId, CreateSubLectureRequest request) {
        Member findMember = findMember(memberId);
        checkPermission(findMember);
        Lecture lecture = getLecture(lectureId);
        SubLecture subLecture = createSubLecture(lecture, request);
        subLectureRepository.save(subLecture);
    }

    private Lecture getLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CLASS_NOT_FOUND));
    }

    @Transactional
    public void createAssignments(Long subLectureId, Long memberId, CreateAssignmentRequest request) {
        Member findMember = findMember(memberId);
        checkPermission(findMember);
        SubLecture subLecture = getSubLecture(subLectureId);
        Assignment assignment = createAssignment(request, subLecture);
        List<CreateAssignmentQuestionsRequest> questions = request.questions();
        assignmentRepository.save(assignment);
        for (int i = 0; i < questions.size(); i++){
            CreateAssignmentQuestionsRequest questionsRequest  = questions.get(i);
            AssignmentQuestion assignmentQuestion = createAssignmentQuestion(assignment, questionsRequest);
            assignmentQuestionRepository.save(assignmentQuestion);
        }
    }

    private static AssignmentQuestion createAssignmentQuestion(Assignment assignment, CreateAssignmentQuestionsRequest questionsRequest) {
        return AssignmentQuestion.builder()
                .assignment(assignment)
                .assignmentAnswerType(AssignmentAnswerType.of(questionsRequest.assignmentAnswerType()))
                .body(questionsRequest.body())
                .orderNumber(questionsRequest.orderNumber())
                .build();
    }

    private static Assignment createAssignment(CreateAssignmentRequest request, SubLecture subLecture) {
        return Assignment.builder()
                .title(request.title())
                .body(request.body())
                .subLecture(subLecture)
                .build();
    }

    private SubLecture getSubLecture(Long subLectureId) {
        return subLectureRepository.findById(subLectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.SUB_CLASS_NOT_FOUND));
    }

    private SubLecture createSubLecture(Lecture lecture, CreateSubLectureRequest request) {
        return SubLecture.builder()
                .title(request.title())
                .description(request.description())
                .orderNumber(request.OrderNumber())
                .lecture(lecture)
                .build();
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
