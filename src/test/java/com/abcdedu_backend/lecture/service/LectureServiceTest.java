package com.abcdedu_backend.lecture.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.lecture.dto.response.GetAssignmentFileUrlResponse;
import com.abcdedu_backend.lecture.dto.response.GetAssignmentResponseV1;
import com.abcdedu_backend.lecture.dto.response.GetClassResponse;
import com.abcdedu_backend.lecture.entity.AssignmentFile;
import com.abcdedu_backend.lecture.entity.AssignmentType;
import com.abcdedu_backend.lecture.entity.Lecture;
import com.abcdedu_backend.lecture.entity.SubLecture;
import com.abcdedu_backend.lecture.repository.AssignmentFileRepository;
import com.abcdedu_backend.lecture.repository.LectureRepository;
import com.abcdedu_backend.lecture.repository.SubLectureRepository;
import com.abcdedu_backend.memberv2.application.MemberService;
import com.abcdedu_backend.memberv2.adapter.out.entity.MemberEntity;
import com.abcdedu_backend.memberv2.application.domain.MemberRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private SubLectureRepository subLectureRepository;
    @Mock
    private AssignmentFileRepository assignmentFileRepository;
    @Mock
    private FileHandler fileHandler;
    @Mock
    private MemberService memberService;

    @InjectMocks
    private LectureService target;

    @Test
    void 클래스_조회를_한다() {
        // given
        Lecture lecture = createLecture();
        SubLecture subLecture = createSubLecture(lecture);
        lecture.getSubLectures().add(subLecture);

        doReturn(List.of(lecture, lecture, lecture)).when(lectureRepository).findAll();

        // when
        List<GetClassResponse> result = target.getLectures();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        GetClassResponse response = result.get(0);
        assertThat(response.title()).isEqualTo(lecture.getTitle());
        assertThat(response.subTitle()).isEqualTo(lecture.getSubTitle());
        assertThat(response.description()).isEqualTo(lecture.getDescription());
        assertThat(response.subClasses()).isNotNull();
        assertThat(response.subClasses()).hasSize(1);
        assertThat(response.subClasses().get(0).title()).isEqualTo(subLecture.getTitle());

        verify(lectureRepository, times(1)).findAll();
    }

    @ParameterizedTest
    @EnumSource(AssignmentType.class)
    void 클래스_평가를_성공적으로_업로드한다(AssignmentType assignmentType) {
        // Given: Mock 데이터 준비
        Long subLectureId = 1L;
        Long memberId = 1L;
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "".getBytes());

        MemberEntity member = createMember();
        SubLecture subLecture = createSubLecture(createLecture());

        // When: 의존성 모킹 설정
        doReturn(member).when(memberService).checkMember(memberId);
        doReturn(subLecture).when(subLectureRepository).getById(subLectureId);
        doReturn("s3/object/key").when(fileHandler).upload(mockFile, FileDirectory.of(assignmentType.getType()), subLecture.getSubLectureName());


        // Then: 과제 파일을 성공적으로 업로드하고 저장했는지 검증
        target.createAssignmentsFile(subLectureId, memberId, assignmentType, mockFile);

        verify(memberService, times(1)).checkMember(memberId);
        verify(subLectureRepository, times(1)).getById(subLectureId);
        verify(fileHandler, times(1)).upload(mockFile, FileDirectory.of(assignmentType.getType()), subLecture.getSubLectureName());
        verify(assignmentFileRepository, times(1)).save(any(AssignmentFile.class));
    }

    @Test
    void 중복된_파일이_존재하면_예외가_발생한다() {
        // given
        Long subLectureId = 1L;
        Long memberId = 1L;
        AssignmentType assignmentType = AssignmentType.DATA;
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "".getBytes());

        MemberEntity member = createMember();
        SubLecture subLecture = createSubLecture(createLecture());
        subLecture.getAssignmentFiles().add(AssignmentFile.of(subLecture, assignmentType, "objectKey"));

        // when
        doReturn(member).when(memberService).checkMember(memberId);
        doReturn(subLecture).when(subLectureRepository).getById(subLectureId);

        Assertions.assertThrows(ApplicationException.class, () -> target.createAssignmentsFile(subLectureId, memberId, assignmentType, mockFile));

        // then
        verify(memberService, times(1)).checkMember(memberId);
        verify(subLectureRepository, times(1)).getById(subLectureId);
        verify(assignmentFileRepository, never()).save(any(AssignmentFile.class));
    }

    @Test
    void 평가파일_리스트를_조회한다() {
        // Given
        Long subLectureId = 1L;
        AssignmentFile assignmentFile1 = createAssignmentFile(AssignmentType.DATA, 1L);
        AssignmentFile assignmentFile2 = createAssignmentFile(AssignmentType.EXAM, 2L);
        List<AssignmentFile> mockAssignmentFiles = List.of(assignmentFile1, assignmentFile2);
        SubLecture mockSubLecture = mock(SubLecture.class);

        doReturn(mockAssignmentFiles).when(mockSubLecture).getAssignmentFiles();
        doReturn(mockSubLecture).when(subLectureRepository).getById(subLectureId);

        // When
        List<GetAssignmentResponseV1> result = target.getAssignments(subLectureId);

        // Then
        assertThat(result).isNotNull();
        assertThat(assignmentFile1.getId()).isEqualTo(result.get(0).assignmentFileId());
        assertThat(assignmentFile2.getId()).isEqualTo(result.get(1).assignmentFileId());
        assertThat(assignmentFile1.getAssignmentType().getType()).isEqualTo(result.get(0).assignmentType());
        assertThat(assignmentFile2.getAssignmentType().getType()).isEqualTo(result.get(1).assignmentType());
    }

    @ParameterizedTest
    @EnumSource(value = AssignmentType.class, names = "THEORY", mode = EnumSource.Mode.EXCLUDE)
    void 평가파일을_조회하고_presignedURL을_가져온다_이론제외(AssignmentType assignmentType) {
        // Given
        Long memberId = 1L;
        Long assignmentFileId = 1L;
        MemberEntity mockMember = mock(MemberEntity.class);
        AssignmentFile assignmentFile = createAssignmentFile(assignmentType, 1L);
        String presignedUrl = "http://example.com/presigned-url";

        doReturn(mockMember).when(memberService).checkMember(memberId);
        doReturn(assignmentFile).when(assignmentFileRepository).getById(assignmentFileId);
        doReturn(presignedUrl).when(fileHandler).getPresignedUrl(assignmentFile.getObjectKey());

        // When
        GetAssignmentFileUrlResponse result = target.getAssignmentFileUrl(memberId, assignmentFileId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.filePresignedUrl()).isEqualTo(presignedUrl);
    }

    @Test
    void 이론파일을_조회하고_presignedURL을_가져온다() {
        // Given
        Long memberId = 1L;
        Long assignmentFileId = 1L;
        MemberEntity mockMember = createMember();
        AssignmentFile assignmentFile = createAssignmentFile(AssignmentType.THEORY, 1L);
        String presignedUrl = "http://example.com/presigned-url";

        doReturn(mockMember).when(memberService).checkMember(memberId);
        doReturn(assignmentFile).when(assignmentFileRepository).getById(assignmentFileId);
        doReturn(presignedUrl).when(fileHandler).getPresignedUrl(assignmentFile.getObjectKey());

        // When
        GetAssignmentFileUrlResponse result = target.getAssignmentFileUrl(memberId, assignmentFileId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.filePresignedUrl()).isEqualTo(presignedUrl);
    }

    @ParameterizedTest
    @EnumSource(value = AssignmentType.class, names = "THEORY")
    void 이론파일은_새싹등급일때_조회하지_못한다() {
        // Given
        Long memberId = 1L;
        Long assignmentFileId = 1L;
        MemberEntity mockMember = createBasicMember();
        AssignmentFile assignmentFile = createAssignmentFile(AssignmentType.THEORY, 1L);

        doReturn(mockMember).when(memberService).checkMember(memberId);
        doReturn(assignmentFile).when(assignmentFileRepository).getById(assignmentFileId);

        // When & Then
        Assertions.assertThrows(ApplicationException.class,
                () -> target.getAssignmentFileUrl(memberId, assignmentFileId));
    }

    @ParameterizedTest
    @EnumSource(value = AssignmentType.class, names = "THEORY")
    void 클래스_이론_평가를_성공적으로_업로드한다(AssignmentType assignmentType) {
        // Given
        Long subLectureId = 1L;
        Long memberId = 1L;
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "".getBytes());

        MemberEntity member = createMember();
        SubLecture subLecture = createSubLecture(createLecture());

        // When
        doReturn(member).when(memberService).checkMember(memberId);
        doReturn(subLecture).when(subLectureRepository).getById(subLectureId);
        doReturn("s3/object/key").when(fileHandler).upload(mockFile, FileDirectory.of(assignmentType.getType()), subLecture.getSubLectureName());

        // Then
        target.createAssignmentsFile(subLectureId, memberId, assignmentType, mockFile);

        verify(memberService, times(1)).checkMember(memberId);
        verify(subLectureRepository, times(1)).getById(subLectureId);
        verify(fileHandler, times(1)).upload(mockFile, FileDirectory.of(assignmentType.getType()), subLecture.getSubLectureName());
        verify(assignmentFileRepository, times(1)).save(any(AssignmentFile.class));
    }

    @Test
    void 평가파일을_업데이트한다() {
        // Given
        Long assignmentFileId = 1L;
        MultipartFile mockFile = mock(MultipartFile.class);
        SubLecture subLecture = createSubLecture(createLecture());
        AssignmentFile assignmentFile = createAssignmentFile(AssignmentType.ANSWER, assignmentFileId, subLecture);
        String updatedObjectKey = "updateObjectKey";

        doReturn(assignmentFile).when(assignmentFileRepository).getById(assignmentFileId);
        doReturn(updatedObjectKey).when(fileHandler).upload(mockFile, FileDirectory.of(assignmentFile.getAssignmentType().getType()), assignmentFile.getSubLecture().getSubLectureName());

        // When
        target.updateAssignmentFile(assignmentFileId, mockFile);

        // Then
        verify(assignmentFileRepository, times(1)).getById(assignmentFileId);
        verify(fileHandler, times(1)).upload(mockFile,
                FileDirectory.of(assignmentFile.getAssignmentType().getType()),
                assignmentFile.getSubLecture().getSubLectureName());
    }

    private AssignmentFile createAssignmentFile(AssignmentType type, Long id) {
        return AssignmentFile.builder()
                .id(id)
                .assignmentType(type)
                .objectKey("objectKey")
                .build();
    }

    private AssignmentFile createAssignmentFile(AssignmentType type, Long id, SubLecture subLecture) {
        return AssignmentFile.builder()
                .id(id)
                .assignmentType(type)
                .objectKey("objectKey")
                .subLecture(subLecture)
                .build();
    }

    private MemberEntity createMember() {
        return MemberEntity.builder()
                .id(1L)
                .name("관리자")
                .role(MemberRole.ADMIN)
                .build();
    }

    private MemberEntity createBasicMember() {
        return MemberEntity.builder()
                .id(1L)
                .name("새싹")
                .role(MemberRole.BASIC)
                .build();
    }

    private Lecture createLecture() {
        return Lecture.builder()
                .id(1L)
                .title("A")
                .subTitle("인공지능 강의")
                .description("인공지능 강의입니다.")
                .subLectures(new ArrayList<>())
                .build();
    }

    private SubLecture createSubLecture(Lecture lecture) {
        return SubLecture.builder()
                .id(1L)
                .title("학생을 위한 머신러닝")
                .orderNumber(1)
                .description("인공지능에게 대체되지 않는 ~~~")
                .lecture(lecture)
                .assignmentFiles(new ArrayList<>())
                .build();
    }
}