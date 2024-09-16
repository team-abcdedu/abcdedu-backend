package com.abcdedu_backend.lecture.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.lecture.dto.response.*;
import com.abcdedu_backend.lecture.dto.*;
import com.abcdedu_backend.lecture.entity.*;
import com.abcdedu_backend.lecture.repository.*;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AssignmentFileRepository assignmentFileRepository;
    private final AssignmentAnswerFileRepository assignmentAnswerFileRepository;

    public List<GetClassResponse> getLectures() {
        List<Lecture> lectures = lectureRepository.findAll();

        List<GetClassResponse> getClassesResponse = lectures.stream()
                .map(this::convertToGetClassResponse)
                .collect(Collectors.toUnmodifiableList());

        return getClassesResponse;
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

    private GetClassResponse convertToGetClassResponse(Lecture lecture) {
        return GetClassResponse.builder()
                .title(lecture.getTitle())
                .subTitle(lecture.getSubTitle())
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
        if (assignmentFile.getAssignmentAnswerFile() == null){
            return GetAssignmentFileUrlResponse.builder()
                    .filePresignedUrl(presignedUrl)
                    .build();
        }
        return new GetAssignmentFileUrlResponse(presignedUrl, assignmentFile.getAssignmentAnswerFile().getId());
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
                FileDirectory.ASSIGNMENT_EXAM_ANSWER_FILE,
                assignmentFile.getSubLecture().getSubLectureName());

        AssignmentAnswerFile assignmentAnswerFile = AssignmentAnswerFile.builder()
                .objectKey(objectKey)
                .assignmentFile(assignmentFile)
                .build();

        assignmentAnswerFileRepository.save(assignmentAnswerFile);
    }

    public GetAssignmentAnswerFileUrlResponse getAssignmentAnswerFileUrl(Long memberId, Long assignmentAnswerFileId) {
        Member findMember = memberService.checkMember(memberId);
        AssignmentAnswerFile assignmentAnswerFile = findAssignmentAnswerFile(assignmentAnswerFileId);
        checkBasicPermission(findMember);
        String objectKey = assignmentAnswerFile.getObjectKey();
        String presignedUrl = fileHandler.getPresignedUrl(objectKey);
        return new GetAssignmentAnswerFileUrlResponse(presignedUrl);
    }

    private AssignmentAnswerFile findAssignmentAnswerFile(Long assignmentAnswerFileId) {
        return assignmentAnswerFileRepository.findById(assignmentAnswerFileId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_ANSWER_FILE_NOT_FOUND));
    }

    @Transactional
    public void updateAssignmentFile(Long memberId, Long assignmentFileId, MultipartFile file) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        AssignmentFile assignmentFile = findAssignmentFile(assignmentFileId);

        String objectKey = fileHandler.upload(
                file,
                FileDirectory.of(assignmentFile.getAssignmentType().getType()),
                assignmentFile.getSubLecture().getSubLectureName()
        );

        assignmentFile.updateObjectKey(objectKey);
    }

    @Transactional
    public void updateAssignmentAnswerFile(Long memberId, Long assignmentAnswerFileId, MultipartFile file) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        AssignmentAnswerFile assignmentAnswerFile = findAssignmentAnswerFile(assignmentAnswerFileId);

        String objectKey = fileHandler.upload(
                file,
                FileDirectory.ASSIGNMENT_EXAM_ANSWER_FILE,
                assignmentAnswerFile.getAssignmentFile().getSubLecture().getSubLectureName()
        );

        assignmentAnswerFile.updateObjectKey(objectKey);
    }
}
