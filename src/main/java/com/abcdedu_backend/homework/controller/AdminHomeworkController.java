package com.abcdedu_backend.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/homeworks")
@RequiredArgsConstructor
@Tag(name = "관리자 공통과제 기능", description = " 응답 조회, 과제 생성 및 변경")
public class AdminHomeworkController {



}

