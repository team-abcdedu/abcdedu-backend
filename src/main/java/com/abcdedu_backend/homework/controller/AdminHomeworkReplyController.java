package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.homework.service.HomeworkReplyService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/admin/homeworks")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminHomeworkReplyController {
    private final HomeworkReplyService homeworkReplyService;
    @Operation(summary = "과제 응답 조회 (엑셀)", description = "과제에 대한 응답을 엑셀로 다운로드합니다.")
    @GetMapping("/{homeworkId}/replies/excel")
    public Response<Void> getRepliesAsExcel(@PathVariable Long homeworkId, HttpServletResponse response) throws IOException {
        homeworkReplyService.exportRepliesByMember(response, homeworkId);
        return Response.success();
    }
}

