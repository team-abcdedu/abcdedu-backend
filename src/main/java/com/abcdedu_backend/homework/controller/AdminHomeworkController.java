package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.homework.dto.response.HomeworkReplyGetRes;
import com.abcdedu_backend.homework.service.HomeworkService;
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
@RequestMapping("/admin/homeworks")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminHomeworkController {
    private final HomeworkService homeworkService;

    @Operation(summary = "과제 응답 조회", description = "과제에 대한 응답을 전체 조회 합니다. (설문과 같은 양식)")
    @GetMapping("/{homeworkId}/replies")
    public Response<HomeworkReplyGetRes> getReplies(@JwtValidation Long memberId, @PathVariable Long homeworkId) {
        HomeworkReplyGetRes homeworkReplyGetRes = homeworkService.getReplies(memberId, homeworkId);
        return Response.success(homeworkReplyGetRes);
    }
}
