package com.abcdedu_backend.lecture.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.lecture.dto.response.*;
import com.abcdedu_backend.lecture.dto.*;
import com.abcdedu_backend.lecture.dto.request.CreateAssignmentAnswerRequest;
import com.abcdedu_backend.lecture.dto.request.CreateAssignmentRequest;
import com.abcdedu_backend.lecture.dto.request.CreateLectureRequest;
import com.abcdedu_backend.lecture.dto.request.CreateSubLectureRequest;
import com.abcdedu_backend.lecture.entity.*;
import com.abcdedu_backend.lecture.repository.*;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class LectureService {

    private final FileHandler fileHandler;

    private final MemberService memberService;

    private final LectureRepository lectureRepository;
    private final SubLectureRepository subLectureRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentQuestionRepository assignmentQuestionRepository;
    private final AssignmentAnswerRepository assignmentAnswerRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final AssignmentFileRepository assignmentFileRepository;
    private final AssignmentAnswerFileRepository assignmentAnswerFileRepository;

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

    private Lecture findLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CLASS_NOT_FOUND));
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

    private AssignmentSubmission createAssignmentSubmission(Assignment assignment, Member findMember) {
        return AssignmentSubmission.builder()
                .assignment(assignment)
                .member(findMember)
                .build();
    }

    public List<GetClassResponse> getLectures() {
        List<Lecture> lectures = lectureRepository.findAll();

        List<GetClassResponse> getClassesResponse = lectures.stream()
                .map(this::convertToGetClassResponse)
                .collect(Collectors.toUnmodifiableList());

        return getClassesResponse;
    }

    public GetAssignmentResponseV2 getAssignment(Long assignmentId) {
        Assignment assignment = findAssignment(assignmentId);
        List<QuestionsDto> questionsDto = convertToQuestionsDtoList(assignment.getAssignmentQuestions());

        return GetAssignmentResponseV2.of(assignment.getTitle(), assignment.getBody(), questionsDto);
    }

    private AssignmentAnswer createAssignmentAnswer(AssignmentSubmission assignmentSubmission, AssignmentQuestion assignmentQuestion, List<CreateAssignmentAnswerDto> answers, int i) {
        return AssignmentAnswer.builder()
                .assignmentSubmission(assignmentSubmission)
                .assignmentQuestion(assignmentQuestion)
                .body(answers.get(i).body())
                .build();
    }

    private Assignment findAssignment(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_ANSWER_TYPE_NOT_FOUND));
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

    private SubLecture findSubLecture(Long subLectureId) {
        return subLectureRepository.findById(subLectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.SUB_CLASS_NOT_FOUND));
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
                .type(request.type())
                .description(request.description())
                .build();
    }

    private void checkAdminPermission(Member member) {
        if (!member.isAdmin()){
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
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

    private GetClassResponse convertToGetClassResponse(Lecture lecture) {
        return GetClassResponse.builder()
                .title(lecture.getTitle())
                .type(lecture.getType())
                .description(lecture.getDescription())
                .subClasses(convertToSubClassesDto(lecture.getSubLectures()))
                .build();
    }

    private List<SubClassDto> convertToSubClassesDto(List<SubLecture> subLectures) {
        return subLectures.stream()
                .map(this::convertToSubClassDto)
                .collect(Collectors.toUnmodifiableList());
    }

    private SubClassDto convertToSubClassDto(SubLecture subLecture) {
        return SubClassDto.builder()
                .title(subLecture.getTitle())
                .orderNumber(subLecture.getOrderNumber())
                .description(subLecture.getDescription())
                .subClassId(subLecture.getId())
                .build();
    }

    public List<GetAssignmentAnswerResponse> getAssignmentAnswers(Pageable pageable, Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        Page<AssignmentSubmission> assignmentSubmissions = assignmentSubmissionRepository.findAllWithMemberAndAssignmentAndSubLecture(pageable);
        return assignmentSubmissions.stream()
                .map(submission -> createGetAssignmentAnswerResponse(submission)
                ).collect(Collectors.toUnmodifiableList());
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

    @Transactional
    public void createAssignmentsFile(Long subLectureId, Long memberId, String assignmentType, MultipartFile file) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        SubLecture findSubLecture = findSubLecture(subLectureId);
        String objectKey = fileHandler.upload(file, FileDirectory.of(assignmentType), findSubLecture.getSubLectureName());

        AssignmentFile assignmentFile = AssignmentFile.builder()
                .objectKey(objectKey)
                .subLecture(findSubLecture)
                .assignmentType(AssignmentType.of(assignmentType))
                .build();

        assignmentFileRepository.save(assignmentFile);
    }

    public List<GetAssignmentResponseV1> getAssignments(Long subLectureId) {
        SubLecture findSublecture = findSubLecture(subLectureId);
        return findSublecture.getAssignmentFiles().stream()
                .map(assignmentFile -> new GetAssignmentResponseV1(assignmentFile.getAssignmentType().getType(), assignmentFile.getId()))
                .collect(Collectors.toUnmodifiableList());
    }

    public GetAssignmentFileUrlResponse getAssignmentFileUrl(Long memberId, Long assignmentFileId) {
        Member findMember = memberService.checkMember(memberId);
        AssignmentFile assignmentFile = findAssignmentFile(assignmentFileId);
        checkTheoryPermission(assignmentFile, findMember);
        checkBasicPermission(findMember);
        String objectKey = assignmentFile.getObjectKey();
        String presignedUrl = fileHandler.getPresignedUrl(objectKey);
        return new GetAssignmentFileUrlResponse(presignedUrl);
    }

    private AssignmentFile findAssignmentFile(Long assignmentFileId) {
        return assignmentFileRepository.findById(assignmentFileId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_FILE_NOT_FOUND));
    }

    private static void checkTheoryPermission(AssignmentFile assignmentFile, Member findMember) {
        if (assignmentFile.getAssignmentType() == AssignmentType.Theory && !findMember.isAdmin()){
            throw new ApplicationException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }
    }

    private static void checkBasicPermission(Member findMember) {
        if (findMember.isBasic()){
            throw new ApplicationException(ErrorCode.BASIC_INVALID_PERMISSION);
        }
    }

    public void createAssignmentAnswerFile(Long assignmentFileId, Long memberId, MultipartFile file) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        AssignmentFile assignmentFile = findAssignmentFile(assignmentFileId);


        String objectKey = fileHandler.upload(
                file,
                FileDirectory.of(assignmentFile.getAssignmentType().getType()),
                "answer/"+assignmentFile.getSubLecture().getSubLectureName());

        AssignmentAnswerFile assignmentAnswerFile = AssignmentAnswerFile.builder()
                .objectKey(objectKey)
                .assignmentFile(assignmentFile)
                .build();

        assignmentAnswerFileRepository.save(assignmentAnswerFile);
    }
}
