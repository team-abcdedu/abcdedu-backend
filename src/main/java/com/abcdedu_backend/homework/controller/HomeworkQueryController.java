package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.common.request.PagingRequest;
import com.abcdedu_backend.common.response.PagedResponse;
import com.abcdedu_backend.homework.dto.HomeworkRes;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/homeworks")
@Tag(name = "공통 과제", description = "공통 과제 관련 API")
public class HomeworkQueryController {

    @GetMapping
    @Operation(summary = "과제 목록 페이징 조회", description = "과제 목록 페이징 조회. 세부내용은 상세 API를 통해 조회합니다.")
    public Response<PagedResponse<HomeworkRes.MainModel>> getHomeworksPaging(
        PagingRequest pagingRequest
    ) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{homeworkId}")
    @Operation(summary = "과제 상세 조회", description = "과제 상세 조회")
    public Response<HomeworkRes.DetailModel> getHomeworkDetail(
        @PathVariable Long homeworkId
    ) {
        throw new UnsupportedOperationException();
    }
}
