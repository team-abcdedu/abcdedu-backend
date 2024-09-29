package com.abcdedu_backend.lecture.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.lecture.dto.response.*;
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

        return lectures.stream()
                .map(GetClassResponse::createGetClassResponse)
                .toList();
    }

    @Transactional
    public void createAssignmentsFile(Long subLectureId, Long memberId, AssignmentType assignmentType, MultipartFile file) {
        Member member = memberService.checkMember(memberId);
        checkAdminPermission(member);
        SubLecture subLecture = subLectureRepository.getById(subLectureId);
        checkDuplicationFile(assignmentType, subLecture);

        String objectKey = fileHandler.upload(file, FileDirectory.of(assignmentType.getType()), subLecture.getSubLectureName());

        AssignmentFile assignmentFile = AssignmentFile.of(subLecture, assignmentType, objectKey);
        assignmentFileRepository.save(assignmentFile);
    }

    public List<GetAssignmentResponseV1> getAssignments(Long subLectureId) {
        SubLecture subLecture = subLectureRepository.getById(subLectureId);
        return subLecture.getAssignmentFiles().stream()
                .map(GetAssignmentResponseV1::of)
                .toList();
    }

    public GetAssignmentFileUrlResponse getAssignmentFileUrl(Long memberId, Long assignmentFileId) {
        Member member = memberService.checkMember(memberId);
        AssignmentFile assignmentFile = assignmentFileRepository.getById(assignmentFileId);
        checkTheoryPermission(assignmentFile, member);
        checkBasicPermission(member);
        String objectKey = assignmentFile.getObjectKey();
        String presignedUrl = fileHandler.getPresignedUrl(objectKey);
        if (assignmentFile.getAssignmentAnswerFile() == null){
            return GetAssignmentFileUrlResponse.builder()
                    .filePresignedUrl(presignedUrl)
                    .build();
        }

        return new GetAssignmentFileUrlResponse(presignedUrl, assignmentFile.getAssignmentAnswerFile().getId());
    }

    @Transactional
    public void createAssignmentAnswerFile(Long assignmentFileId, Long memberId, MultipartFile file) {
        Member member = memberService.checkMember(memberId);
        checkAdminPermission(member);
        AssignmentFile assignmentFile = assignmentFileRepository.getById(assignmentFileId);

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
        AssignmentAnswerFile assignmentAnswerFile = assignmentAnswerFileRepository.getById(assignmentAnswerFileId);
        checkBasicPermission(findMember);
        String objectKey = assignmentAnswerFile.getObjectKey();
        String presignedUrl = fileHandler.getPresignedUrl(objectKey);
        return new GetAssignmentAnswerFileUrlResponse(presignedUrl);
    }

    @Transactional
    public void updateAssignmentFile(Long memberId, Long assignmentFileId, MultipartFile file) {
        Member findMember = memberService.checkMember(memberId);
        checkAdminPermission(findMember);
        AssignmentFile assignmentFile = assignmentFileRepository.getById(assignmentFileId);

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
        AssignmentAnswerFile assignmentAnswerFile = assignmentAnswerFileRepository.getById(assignmentAnswerFileId);

        String objectKey = fileHandler.upload(
                file,
                FileDirectory.ASSIGNMENT_EXAM_ANSWER_FILE,
                assignmentAnswerFile.getAssignmentFile().getSubLecture().getSubLectureName()
        );

        assignmentAnswerFile.updateObjectKey(objectKey);
    }

    private void checkDuplicationFile(AssignmentType assignmentType, SubLecture subLecture) {
        if (subLecture.hasAssignmentType(assignmentType)){
            throw new ApplicationException(ErrorCode.ASSIGNMENT_FILE_DUPLICATION);
        }
    }

    private void checkAdminPermission(Member member) {
        if (!member.isAdmin()){
            throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        }
    }

    private void checkTheoryPermission(AssignmentFile assignmentFile, Member findMember) {
        if (assignmentFile.getAssignmentType() == AssignmentType.THEORY && !findMember.isAdmin()){
            throw new ApplicationException(ErrorCode.ADMIN_INVALID_PERMISSION);
        }
    }

    private void checkBasicPermission(Member findMember) {
        if (findMember.isBasic()){throw new ApplicationException(ErrorCode.BASIC_INVALID_PERMISSION);
        }
    }
}
