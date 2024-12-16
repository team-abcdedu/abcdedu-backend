package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.homework.dto.request.HomeworkReplyReadReq;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.service.HomeworkReplyService;
import com.abcdedu_backend.homework.service.HomeworkService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/homeworks/replies")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminHomeworkReplyController {
    private final HomeworkReplyService homeworkReplyService;
    private final HomeworkService homeworkService;

    @Operation(summary = "과제 응답 조회 (엑셀)", description = "생성 날짜를 선택, 필터링하여 생성합니다.")
    @GetMapping("/excel")
    public Response<Void> exportToExcel(@RequestBody @Valid HomeworkReplyReadReq req, HttpServletResponse response) {
        Homework homework = homeworkService.checkHomework(req.homeworkId());
        homeworkReplyService.exportRepliesByMember(req, response, homework);
        return Response.success();
    }
}
