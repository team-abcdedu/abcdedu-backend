package com.abcdedu_backend.survey.controller;
import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.PageManager;
import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.request.SortRequest;
import com.abcdedu_backend.common.page.response.PagedResponse;
import com.abcdedu_backend.survey.dto.request.SurveyCreateRequest;
import com.abcdedu_backend.survey.dto.response.SurveyListResponse;
import com.abcdedu_backend.survey.dto.response.SurveyRepliesGetResponse;
import com.abcdedu_backend.survey.service.SurveyService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/surveys")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminSurveyController {

    private final SurveyService surveyService;

    @Operation(summary = "설문 등록", description = "여러 개의 질문을 담을 설문을 등록한다.")
    @PostMapping
    public Response<Long> createSurvey(@Valid @RequestBody SurveyCreateRequest request, @JwtValidation Long memberId) {
        Long surveyId = surveyService.createSurvey(request, memberId);
        return Response.success(surveyId);
    }

    @Operation(summary = "설문-질문-응답조회", description = "관리자가 설문별로 보는 설문-질문-응답 조회")
    @GetMapping("/{surveyId}/replies")
    public Response<SurveyRepliesGetResponse> getSurveyReply(@PathVariable Long surveyId) {
        SurveyRepliesGetResponse surveyReplys = surveyService.getSurveyReplies(surveyId);
        return Response.success(surveyReplys);
    }

    @Operation(summary = "설문 리스트 조회")
    @GetMapping
    public Response<PagedResponse<SurveyListResponse>> getSurveys(@JwtValidation Long memberId, PagingRequest pagingRequest, SortRequest sortRequest) {
        Page<SurveyListResponse> surveys = surveyService.getSurveys(memberId, new PageManager(pagingRequest, sortRequest).makePageRequest());
        return Response.success(PagedResponse.from(surveys));
    }

    @Operation(summary = "설문 삭제")
    @DeleteMapping("/{surveyId}")
    public Response<Void> deleteSurvey(@JwtValidation Long memberId, @PathVariable Long surveyId) {
        surveyService.deleteSurvey(memberId, surveyId);
        return Response.success();
    }
}
