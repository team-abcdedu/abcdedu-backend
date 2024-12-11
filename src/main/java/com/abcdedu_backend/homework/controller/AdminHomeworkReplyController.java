package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.homework.dto.request.HomeworkReplyReadReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/homeworks/replies")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminHomeworkReplyController {

    @Operation(summary = "과제 응답 조회 (엑셀)", description = "생성 날짜를 선택, 필터링하여 생성합니다.")
    @GetMapping
    public void exportToExcel() {

    }
}
