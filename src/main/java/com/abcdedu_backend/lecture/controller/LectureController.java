package com.abcdedu_backend.lecture.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.lecture.dto.response.*;
import com.abcdedu_backend.lecture.service.LectureService;
import com.abcdedu_backend.utils.FileUtil;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @Operation(summary = "클래스 조회", description = "클래스(A, B, C, D) 및 서브 클래스를 조회합니다.")
    @GetMapping
    public Response<List<GetClassResponse>> GetLectures(){
        List<GetClassResponse> response = lectureService.getLectures();
        return Response.success(response);
    }

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content)
    @Operation(summary = "평가 파일 (시험/실습/프로젝트/이론) 등록 (admin)", description = "평가 파일(시험/실습/프로젝트/이론)을 등록합니다.")
    @PostMapping("/sub-lecture/{subLectureId}/file")
    public Response<Void> createAssignmentFile(@PathVariable Long subLectureId,
                                               @JwtValidation Long memberId,
                                               @RequestParam String assignmentType,
                                               @RequestPart("file") MultipartFile multipartFile){

        File file = FileUtil.convertToFile(multipartFile);
        lectureService.createAssignmentsFile(subLectureId, memberId, assignmentType, file);
        return Response.success();
    }

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content)
    @Operation(summary = "평가 파일 (시험/실습/프로젝트/이론) 문제지 등록 (admin)", description = "평가 파일(시험/실습/프로젝트/이론) 문제지를 등록합니다.")
    @PostMapping("/assignment-file/{assignmentFileId}/answer")
    public Response<Void> createAssignmentAnswerFile(@PathVariable Long assignmentFileId,
                                               @JwtValidation Long memberId,
                                               @RequestPart("file") MultipartFile multipartFile){
        File file = FileUtil.convertToFile(multipartFile);
        lectureService.createAssignmentAnswerFile(assignmentFileId, memberId, file);
        return Response.success();
    }

    @Operation(summary = "평가 파일(시험/실습/프로젝트/이론) 리스트 조회", description = "평가 파일(시험/실습/프로젝트/이론) 리스트를 조회합니다.")
    @GetMapping("/sub-lecture/{subLectureId}")
    public Response<List<GetAssignmentResponseV1>> getAssignments(@PathVariable Long subLectureId){
        List<GetAssignmentResponseV1> response =lectureService.getAssignments(subLectureId);
        return Response.success(response);
    }

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다.", content = @Content)
    @Operation(summary = "평가 파일 (시험/실습/프로젝트/이론) 조회", description = "평가 파일(시험/실습/프로젝트/이론)을 조회합니다.")
    @GetMapping("/file/{assignmentFileId}")
    public Response<GetAssignmentFileUrlResponse> getAssignmentFileUrl(@PathVariable Long assignmentFileId, @JwtValidation Long memberId){
        GetAssignmentFileUrlResponse response = lectureService.getAssignmentFileUrl(memberId, assignmentFileId);
        return Response.success(response);
    }

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다.", content = @Content)
    @Operation(summary = "평가 파일 (시험/실습/프로젝트/이론) 문제지 조회", description = "평가 파일(시험/실습/프로젝트/이론) 문제지를 조회합니다.")
    @GetMapping("/answer-file/{assignmentAnswerFileId}")
    public Response<GetAssignmentAnswerFileUrlResponse> getAssignmentAnswerFileUrl(@PathVariable Long assignmentAnswerFileId, @JwtValidation Long memberId){
        GetAssignmentAnswerFileUrlResponse response = lectureService.getAssignmentAnswerFileUrl(memberId, assignmentAnswerFileId);
        return Response.success(response);
    }


}
