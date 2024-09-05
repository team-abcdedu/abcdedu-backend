package com.abcdedu_backend.lecture.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.lecture.dto.request.CreateAssignmentAnswerRequest;
import com.abcdedu_backend.lecture.dto.request.CreateAssignmentRequest;
import com.abcdedu_backend.lecture.dto.request.CreateLectureRequest;
import com.abcdedu_backend.lecture.dto.request.CreateSubLectureRequest;
import com.abcdedu_backend.lecture.dto.response.*;
import com.abcdedu_backend.lecture.service.LectureService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = @Content),
        @ApiResponse(responseCode = "404", description = "~를 찾을 찾을 수 없습니다. (유저, 과제, 클래스 등)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
@Tag(name = "클래스 기능", description = "클래스 관련 api 입니다")
public class LectureController {

    private final LectureService lectureService;

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content)
    @Operation(summary = "클래스등록", description = "클래스(A, B, C, D)를 등록합니다.")
    @PostMapping
    public Response<Void> createLecture(@JwtValidation Long memberId,
                                        @Valid @RequestBody CreateLectureRequest createLectureRequest){
        lectureService.createLecture(memberId, createLectureRequest);
        return Response.success();
    }

    @Operation(summary = "클래스 조회", description = "클래스(A, B, C, D) 및 서브 클래스를 조회합니다.")
    @GetMapping
    public Response<List<GetClassResponse>> GetLectures(){
        List<GetClassResponse> response = lectureService.getLectures();
        return Response.success(response);
    }

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content)
    @Operation(summary = "서브 클래스 등록", description = "서브 클래스를 등록합니다.")
    @PostMapping("/{lectureId}")
    public Response<Void> createSubLecture(@PathVariable Long lectureId,
                                           @JwtValidation Long memberId,
                                           @Valid @RequestBody CreateSubLectureRequest createSubLectureRequest){
        lectureService.createSubLecture(lectureId, memberId, createSubLectureRequest);
        return Response.success();
    }

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content)
    @Operation(summary = "시험 등록", description = "클래스 시험을 등록합니다.")
    @PostMapping("/{subLectureId}/assignments")
    public Response<Void> createAssignments(@PathVariable Long subLectureId,
                                            @JwtValidation Long memberId,
                                            @Valid @RequestBody CreateAssignmentRequest createAssignmentRequest){
        lectureService.createAssignments(subLectureId, memberId, createAssignmentRequest);
        return Response.success();
    }

    @Operation(summary = "시험 제출", description = "시험을 제출합니다.")
    @PostMapping("/assignments/{assignmentId}")
    public Response<Void> saveAssignmentAnswer(@PathVariable Long assignmentId,
                                               @JwtValidation Long memberId,
                                               @Valid @RequestBody CreateAssignmentAnswerRequest createAssignmentAnswerRequest){
        lectureService.createAssignmentsAnswer(assignmentId, memberId, createAssignmentAnswerRequest);
        return Response.success();
    }

    @Operation(summary = "시험 조회", description = "시험을 조회합니다.")
    @GetMapping("/assignments/{assignmentId}")
    public Response<GetAssignmentResponseV2> getAssignment(@PathVariable Long assignmentId){
        GetAssignmentResponseV2 response = lectureService.getAssignment(assignmentId);
        return Response.success(response);
    }

    @Operation(summary = "시험 제출 목록 조회 (admin)", description = "시험 제출 목록을 조회합니다.")
    @GetMapping("/assignments/submissions")
    public Response<List<GetAssignmentAnswerResponse>> getAssignmentAnswers(@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                                            @JwtValidation Long memberId){
        List<GetAssignmentAnswerResponse> response = lectureService.getAssignmentAnswers(pageable, memberId);
        return Response.success(response);
    }

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content)
    @Operation(summary = "평가 파일 (시험/실습/프로젝트/이론) 등록 (admin)", description = "평가 파일(시험/실습/프로젝트/이론)을 등록합니다.")
    @PostMapping("/sub-lecture/{subLectureId}/file")
    public Response<Void> createAssignmentFile(@PathVariable Long subLectureId,
                                               @JwtValidation Long memberId,
                                               @RequestParam String assignmentType,
                                               @RequestPart("file") MultipartFile file){
        lectureService.createAssignmentsFile(subLectureId, memberId, assignmentType, file);
        return Response.success();
    }

    @Operation(summary = "평가 파일(시험/실습/프로젝트/이론) 리스트 조회", description = "평가 파일(시험/실습/프로젝트/이론) 리스트를 조회합니다.")
    @GetMapping("/sub-lecture/{subLectureId}")
    public Response<List<GetAssignmentResponseV1>> getAssignments(@PathVariable Long subLectureId){
        List<GetAssignmentResponseV1> response =lectureService.getAssignments(subLectureId);
        return Response.success(response);
    }

    @Operation(summary = "평가 파일 (시험/실습/프로젝트/이론) 조회", description = "평가 파일(시험/실습/프로젝트/이론)을 조회합니다.")
    @GetMapping("/file/{assignmentFileId}")
    public Response<GetAssignmentFileUrlResponse> getAssignmentFileUrl(@PathVariable Long assignmentFileId, @JwtValidation Long memberId){
        GetAssignmentFileUrlResponse response = lectureService.getAssignmentFileUrl(memberId, assignmentFileId);
        return Response.success(response);
    }
}
