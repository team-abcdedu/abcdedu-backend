package com.abcdedu_backend.homework.controller;
import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.PageManager;
import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.request.SortRequest;
import com.abcdedu_backend.common.page.response.PagedResponse;
import com.abcdedu_backend.homework.dto.request.RepresentativeRegisterRequest;
import com.abcdedu_backend.homework.dto.request.modifyRequest;
import com.abcdedu_backend.homework.dto.request.registerRequest;
import com.abcdedu_backend.homework.dto.response.HomeworkRes;
import com.abcdedu_backend.homework.service.HomeworkAdminService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/admin/homeworks")
@RequiredArgsConstructor
@Tag(name = "관리자 기능 - 공통과제", description = "공통 과제에 대한 관리자 기능을 모아두었습니다.")
public class AdminHomeworkController {
    private final HomeworkAdminService homeworkAdminService;

    @Operation(summary = "대표 공통과제 등록", description = "메인에 과제로 띄울 대표 버전을 선택한다.")
    @PostMapping("/representative")
    public Response<Void> registerRepresentative(@JwtValidation Long registerId, @Valid @RequestBody RepresentativeRegisterRequest request) {
        homeworkAdminService.registerAsRepresentative(registerId, request);
        return Response.success();
    }

    @Operation(summary = "과제 목록", description = "과제 목록을 조회 합니다.")
    @GetMapping
    public Response<PagedResponse<HomeworkRes>> read(PagingRequest pagingRequest, SortRequest sortRequest) {
        PageRequest pageRequest = new PageManager(pagingRequest, sortRequest).makePageRequest();
        Page<HomeworkRes> homeworks = homeworkAdminService.getHomeworks(pageRequest);
        return Response.success(PagedResponse.from(homeworks));
    }

    @Operation(summary = "공통 과제 상세 조회", description = "개발중입니다. 아직 사용할 수 없습니다. 과제, 질문이 함께 조회된다.")
    @GetMapping("/{homeworkId}")
    public Response<Void> getHomework(@PathVariable Long homeworkId) {
        return Response.success();
    }

    @Operation(summary = "공통 과제 등록", description = "여러 개의 질문을 담은 과제를 등록한다.")
    @PostMapping
    public Response<Long> createHomework(@Valid @RequestBody registerRequest request) {
        return Response.success();
    }

    @Operation(summary = "공통 과제 수정", description = "과제 내용을 수정한다.")
    @PatchMapping
    public Response<Void> updateHomework(@Valid @RequestBody modifyRequest request) {
        return Response.success();
    }

    @Operation(summary = "공통 과제 삭제", description = "개발중입니다. 아직 사용할 수 없습니다.")
    @DeleteMapping("/{homeworkId}")
    public Response<Void> deleteSurvey(@PathVariable Long homeworkId) {
        return Response.success();
    }
}
