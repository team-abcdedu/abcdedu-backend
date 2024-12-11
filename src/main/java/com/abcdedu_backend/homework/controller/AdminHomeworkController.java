package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.homework.dto.response.HomeworkRes;
import com.abcdedu_backend.homework.service.HomeworkService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/homeworks")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminHomeworkController {
    private final HomeworkService homeworkService;
    @Operation(summary = "과제 목록", description = "과제 목록을 조회 합니다.")
    @GetMapping
    public Response<List<HomeworkRes>> readList() {
        return Response.success(homeworkService.getHomeworks());
    }

}
