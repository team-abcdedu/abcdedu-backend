package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.request.SortRequest;
import com.abcdedu_backend.homework.service.HomeworkService;
import com.abcdedu_backend.survey.dto.request.SurveyCreateRequest;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/homeworks")
@RequiredArgsConstructor
@Tag(name = "관리자 기능 - 공통과제", description = "제작중입니다.")
public class AdminHomeworkController {
    private final HomeworkService homeworkService;

    @Operation(summary = "공통 과제 등록", description = "여러 개의 질문을 담은 과제를 등록한다.")
    @PostMapping
    public Response<Long> createHomework(@Valid @RequestBody SurveyCreateRequest request, @JwtValidation Long memberId) {
        return Response.success();
    }

    @Operation(summary = "공통 과제 리스트 조회")
    @GetMapping
    public Response<Void> getHomeworks(@JwtValidation Long memberId, PagingRequest pagingRequest, SortRequest sortRequest) {
        return Response.success();
    }

    @Operation(summary = "공통 과제 상세 조회", description = "과제, 질문이 함께 조회된다.")
    @GetMapping("/{homeworkId}")
    public Response<Void> getHomework(@JwtValidation Long memberId, @PathVariable Long homeworkId) {
        return Response.success();
    }

    @Operation(summary = "대표 과제 등록", description = "과제로 띄울 대표 버전을 선택한다.")
    @PostMapping("/{homeworkId}/representative")
    public Response<Void> registerRepresentative(@PathVariable Long homeworkId, @JwtValidation Long memberId) {
        homeworkService.registerRepresentative(homeworkId, memberId);
        return Response.success();
    }

    @Operation(summary = "공통 과제 수정", description = "과제 내용을 수정한다.")
    @PatchMapping("/{homeworkId}")
    public Response<Void> updateHomework(@PathVariable Long homeworkId, @JwtValidation Long memberId) {
        return Response.success();
    }

    @Operation(summary = "공통 과제 삭제")
    @DeleteMapping("/{homeworkId}")
    public Response<Void> deleteSurvey(@JwtValidation Long memberId, @PathVariable Long homeworkId) {
        return Response.success();
    }

}
