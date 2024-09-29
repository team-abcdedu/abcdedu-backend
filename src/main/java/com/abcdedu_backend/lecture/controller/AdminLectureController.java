package com.abcdedu_backend.lecture.controller;


import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.lecture.entity.AssignmentType;
import com.abcdedu_backend.lecture.service.LectureService;
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

@Slf4j
@RestController
@RequestMapping("/admin/lectures")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = @Content),
        @ApiResponse(responseCode = "404", description = "~를 찾을 찾을 수 없습니다. (유저, 과제, 클래스 등)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
@Tag(name = "관리자 기능", description = "관리자가 사용할 수 있는 api 입니다")
public class AdminLectureController {

    private final LectureService lectureService;

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content)
    @ApiResponse(responseCode = "500", description = "S3 업로드에 실패하였습니다.", content = @Content)
    @Operation(summary = "평가 파일 (이론/시험/자료) 등록 (admin)", description = """
            평가 파일 (이론/시험/자료/시험지)를 등록합니다.
            AssignmentType 종류는 `THEORY(이론)`, `EXAM(시험)`, `DATA(자료)`, `ANSWER(시험지)` 입니다""")
    @PostMapping("/sub-lecture/{subLectureId}/file")
    public Response<Void> createAssignmentFile(@PathVariable Long subLectureId,
                                               @RequestParam AssignmentType assignmentType,
                                               @RequestPart("file") MultipartFile multipartFile){
        lectureService.createAssignmentsFile(subLectureId, assignmentType, multipartFile);
        return Response.success();
    }

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다.", content = @Content)
    @ApiResponse(responseCode = "500", description = "S3 업로드에 실패하였습니다.", content = @Content)
    @Operation(summary = "평가 파일 (이론/시험/자료/시험지) 수정 ", description = "평가 파일 (이론/시험/자료/시험지)를 수정합니다.")
    @PatchMapping("/file/{assignmentFileId}")
    public Response<Void> updateAssignmentFile(@PathVariable Long assignmentFileId,
                                               @RequestPart("file") MultipartFile multipartFile){
        lectureService.updateAssignmentFile(assignmentFileId, multipartFile);
        return Response.success();
    }
}
