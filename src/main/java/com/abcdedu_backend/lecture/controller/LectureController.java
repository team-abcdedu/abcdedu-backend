package com.abcdedu_backend.lecture.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.lecture.dto.CreateLectureRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
@Tag(name = "클래스 기능", description = "클래스 관련 api 입니다")
public class LectureController {

    private final LectureService lectureService;

    @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content)
    @Operation(summary = "클래스등록", description = "클래스(A, B, C, D)를 등록합니다.")
    @PostMapping("/type")
    public Response<Void> createLecture(@JwtValidation Long memberId, @Valid @RequestBody CreateLectureRequest createLectureRequest){
        lectureService.createLecture(memberId, createLectureRequest);
        return Response.success();
    }
}
