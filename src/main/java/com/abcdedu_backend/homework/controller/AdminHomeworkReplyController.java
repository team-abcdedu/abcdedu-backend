package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.homework.dto.request.HomeworkReplyReadReq;
import com.abcdedu_backend.homework.entity.Homework;
import com.abcdedu_backend.homework.service.HomeworkReplyService;
import com.abcdedu_backend.homework.service.HomeworkService;
import com.abcdedu_backend.utils.Response;
import com.abcdedu_backend.utils.exportable.ExportDataProvider;
import com.abcdedu_backend.utils.exportable.Exportable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/admin/homeworks/replies")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminHomeworkReplyController {
    private final HomeworkReplyService homeworkReplyService;
    private final HomeworkService homeworkService;
    private final Exportable exportable;

    @Operation(summary = "과제 응답 조회 (엑셀)", description = "생성 날짜를 선택, 필터링하여 생성합니다." +
            "  요청 예시 : GET /excel?homeworkId=123&fromDate=2024-01-01T00:00:00&toDate=2024-12-31T23:59:59")
    @GetMapping("/excel")
    public Response<Void> exportToExcel(
            @RequestParam Long homeworkId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime toDate,
            HttpServletResponse response) {
        HomeworkReplyReadReq req = new HomeworkReplyReadReq(homeworkId, fromDate, toDate);
        Homework homework = homeworkService.checkHomework(req.homeworkId());

        ExportDataProvider excelData = homeworkReplyService.exportRepliesByMember(req,homework);
        exportable.export(response, excelData);
        return Response.success();

    }
}
