package com.abcdedu_backend.lecture.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.lecture.dto.CreateAssignmentAnswerDto;
import com.abcdedu_backend.lecture.dto.QuestionsDto;
import com.abcdedu_backend.lecture.dto.request.CreateAssignmentAnswerRequest;
import com.abcdedu_backend.lecture.dto.request.CreateAssignmentRequest;
import com.abcdedu_backend.lecture.dto.request.CreateLectureRequest;
import com.abcdedu_backend.lecture.dto.request.CreateSubLectureRequest;
import com.abcdedu_backend.lecture.dto.response.GetAssignmentAnswerResponse;
import com.abcdedu_backend.lecture.dto.response.GetAssignmentResponseV2;
import com.abcdedu_backend.lecture.entity.*;
import com.abcdedu_backend.lecture.entity.v2.AssignmentAnswer;
import com.abcdedu_backend.lecture.entity.v2.AssignmentAnswerType;
import com.abcdedu_backend.lecture.entity.v2.AssignmentQuestion;
import com.abcdedu_backend.lecture.entity.v2.AssignmentSubmission;
import com.abcdedu_backend.lecture.repository.*;
import com.abcdedu_backend.lecture.repository.v2.AssignmentAnswerRepository;
import com.abcdedu_backend.lecture.repository.v2.AssignmentQuestionRepository;
import com.abcdedu_backend.lecture.repository.v2.AssignmentSubmissionRepository;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class LectureServiceV2 {

    private final MemberService memberService;

    private final LectureRepository lectureRepository;
    private final SubLectureRepository subLectureRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentQuestionRepository assignmentQuestionRepository;
    private final AssignmentAnswerRepository assignmentAnswerRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Transactional
    public void createLecture(Long memberId, CreateLectureRequest request) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        Lecture lecture = createLecture(request);
        lectureRepository.save(lecture);
    }

    @Transactional
    public void createSubLecture(Long lectureId, Long memberId, CreateSubLectureRequest request) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        Lecture findLecture = findLecture(lectureId);
        SubLecture subLecture = createSubLecture(findLecture, request);
        subLectureRepository.save(subLecture);
    }

    @Transactional
    public void createAssignments(Long subLectureId, Long memberId, CreateAssignmentRequest request) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        SubLecture subLecture = findSubLecture(subLectureId);
        Assignment assignment = createAssignment(request, subLecture);
        List<QuestionsDto> questions = request.questions();
        assignmentRepository.save(assignment);
        for (int i = 0; i < questions.size(); i++){
            QuestionsDto questionsRequest  = questions.get(i);
            AssignmentQuestion assignmentQuestion = createAssignmentQuestion(assignment, questionsRequest);
            assignmentQuestionRepository.save(assignmentQuestion);
        }
    }

    @Transactional
    public void createAssignmentsAnswer(Long assignmentId, Long memberId, CreateAssignmentAnswerRequest createAssignmentAnswerRequest) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        Assignment assignment = findAssignment(assignmentId);
        List<AssignmentQuestion> questions = assignment.getAssignmentQuestions();
        List<CreateAssignmentAnswerDto> answers = createAssignmentAnswerRequest.answers();
        AssignmentSubmission assignmentSubmission = createAssignmentSubmission(assignment, findMember);
        assignmentSubmissionRepository.save(assignmentSubmission);
        for (int i = 0; i < questions.size(); i++){
            AssignmentQuestion assignmentQuestion = questions.get(i);
            AssignmentAnswer assignmentAnswer = createAssignmentAnswer(assignmentSubmission, assignmentQuestion, answers, i);
            assignmentAnswerRepository.save(assignmentAnswer);
        }
    }


    public GetAssignmentResponseV2 getAssignment(Long assignmentId) {
        Assignment assignment = findAssignment(assignmentId);
        List<QuestionsDto> questionsDto = convertToQuestionsDtoList(assignment.getAssignmentQuestions());

        return GetAssignmentResponseV2.of(assignment.getTitle(), assignment.getBody(), questionsDto);
    }

    public List<GetAssignmentAnswerResponse> getAssignmentAnswers(Pageable pageable, Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        Page<AssignmentSubmission> assignmentSubmissions = assignmentSubmissionRepository.findAllWithMemberAndAssignmentAndSubLecture(pageable);
        return assignmentSubmissions.stream()
                .map(submission -> createGetAssignmentAnswerResponse(submission)
                ).collect(Collectors.toUnmodifiableList());
    }

    private Assignment findAssignment(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_ANSWER_TYPE_NOT_FOUND));
    }

    private SubLecture createSubLecture(Lecture lecture, CreateSubLectureRequest request) {
        return SubLecture.builder()
                .title(request.title())
                .description(request.description())
                .orderNumber(request.orderNumber())
                .lecture(lecture)
                .build();
    }

    private Lecture createLecture(CreateLectureRequest request) {
        return Lecture.builder()
                .title(request.title())
                .subTitle(request.subTitle())
                .description(request.description())
                .build();
    }

    private AssignmentSubmission createAssignmentSubmission(Assignment assignment, Member findMember) {
        return AssignmentSubmission.builder()
                .assignment(assignment)
                .member(findMember)
                .build();
    }

    private AssignmentAnswer createAssignmentAnswer(AssignmentSubmission assignmentSubmission, AssignmentQuestion assignmentQuestion, List<CreateAssignmentAnswerDto> answers, int i) {
        return AssignmentAnswer.builder()
                .assignmentSubmission(assignmentSubmission)
                .assignmentQuestion(assignmentQuestion)
                .body(answers.get(i).body())
                .build();
    }


    private AssignmentQuestion createAssignmentQuestion(Assignment assignment, QuestionsDto questionsRequest) {
        return AssignmentQuestion.builder()
                .assignment(assignment)
                .assignmentAnswerType(AssignmentAnswerType.of(questionsRequest.assignmentAnswerType()))
                .title(questionsRequest.title())
                .body(questionsRequest.body())
                .orderNumber(questionsRequest.orderNumber())
                .build();
    }

    private Assignment createAssignment(CreateAssignmentRequest request, SubLecture subLecture) {
        return Assignment.builder()
                .title(request.title())
                .body(request.body())
                .subLecture(subLecture)
                .build();
    }

    private List<QuestionsDto> convertToQuestionsDtoList(List<AssignmentQuestion> questions) {
        return questions.stream()
                .map(this::convertToQuestionsDto)
                .collect(Collectors.toUnmodifiableList());
    }

    private QuestionsDto convertToQuestionsDto(AssignmentQuestion question) {
        return QuestionsDto.builder()
                .title(question.getTitle())
                .body(question.getBody())
                .orderNumber(question.getOrderNumber())
                .assignmentAnswerType(question.getAssignmentAnswerType().getType())
                .build();
    }

    private GetAssignmentAnswerResponse createGetAssignmentAnswerResponse(AssignmentSubmission submission) {
        return GetAssignmentAnswerResponse.builder()
                .assignmentId(submission.getAssignment().getId())
                .updatedAt(submission.getUpdatedAt())
                .subClassName(submission.getAssignment().getSubLecture().getTitle())
                .userName(submission.getMember().getName())
                .subClassId(submission.getAssignment().getSubLecture().getId())
                .submissionId(submission.getId())
                .build();
    }

    private Lecture findLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CLASS_NOT_FOUND));
    }

    private SubLecture findSubLecture(Long subLectureId) {
        return subLectureRepository.findById(subLectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.SUB_CLASS_NOT_FOUND));
    }

    private void checkAdminPermission(Member member) {
        if (!member.isAdmin()){
            throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        }
    }
}
