package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/homeworks/{homeworkId}/replies")
@RequiredArgsConstructor
@Tag(name = "관리자 공통과제 응답 관련 기능", description = "응답 조회 및 엑셀로 다운로드")
public class AdminHomeworkReplyController {

    @Operation(summary = "과제 응답 조회", description = "과제에 대한 응답을 전체 조회 합니다.")
    @GetMapping
    public Response<Void> getReplies(@JwtValidation Long memberId, @PathVariable Long homeworkId) {
        return Response.success();
    }

    @Operation(summary = "과제 응답 조회 (엑셀)", description = "과제에 대한 응답을 엑셀로 다운로드합니다.")
    @GetMapping("/excel")
    public Response<Void> getRepliesAsExcel(@JwtValidation Long memberId, @PathVariable Long homeworkId) {
        return Response.success();
    }




}

