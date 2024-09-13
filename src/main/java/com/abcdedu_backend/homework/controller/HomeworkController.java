package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.global.security.LoginUserDetails;
import com.abcdedu_backend.homework.dto.HomeworkReq;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "공통 과제 관리", description = "공통 과제 관리 관련 API")
@RequestMapping("/admin/homeworks")
@RestController
@RequiredArgsConstructor
public class HomeworkController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "과제 생성", description = """
        과제 생성+문제 생성, 이후 `homeworkId`를 반환합니다.
        `options`의 경우 객관식일 경우에만 사용하며, nullable 합니다. 내부 필드는 not-null 입니다.
        Type은 다음과 같습니다. `SUBJECTIVE`, `SINGLE_OPTION`, `MULTIPLE_OPTION`, `SHORT_ANSWER`""")
    public Response<Long> createHomework(
        @AuthenticationPrincipal LoginUserDetails loginUserDetails,
        @Valid @RequestBody HomeworkReq.CreateWithQuestion req
    ) {
        throw new UnsupportedOperationException();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{homeworkId}")
    @Operation(summary = "과제 수정", description = "과제에 대한 PUT 연산을 수행합니다.")
    public Response<Void> updateHomework(
        @AuthenticationPrincipal LoginUserDetails loginUserDetails,
        @PathVariable Long homeworkId,
        @Valid @RequestBody HomeworkReq.Update req
    ) {
        throw new UnsupportedOperationException();
    }



}
