package com.abcdedu_backend.survey.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.request.PagingRequest;
import com.abcdedu_backend.common.response.PagedResponse;
import com.abcdedu_backend.survey.dto.request.SurveyCreateRequest;
import com.abcdedu_backend.survey.dto.request.SurveyReplyCreateRequest;
import com.abcdedu_backend.survey.dto.response.*;
import com.abcdedu_backend.survey.service.SurveyService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
@Tag(name = "설문 기능", description = "설문 관련 api 입니다. 응답 등록, 설문 조회 (응답하기 위한) 외에 모든 기능 관리자만 사용가능 합니다.")
public class SurveyController {

    private final SurveyService surveyService;

    // ====== 설문
    @Operation(summary = "설문 등록", description = "여러 개의 질문을 담을 설문을 등록한다.")
    @PostMapping("/")
    public Response<Long> createSurvey(@Valid @RequestBody SurveyCreateRequest request, @JwtValidation Long memberId) {
        Long surveyId = surveyService.createSurvey(request, memberId);
        return Response.success(surveyId);
    }

    @Operation(summary = "설문 리스트 조회")
    @GetMapping("/")
    public Response<PagedResponse<SurveyListResponse>> getSurveys(@JwtValidation Long memberId, PagingRequest pagingRequest) {
        Page<SurveyListResponse> surveys = surveyService.getSurveys(memberId, pagingRequest.toPageRequest());
        return Response.success(PagedResponse.from(surveys));
    }

    @Operation(summary = "설문 조회", description = "설문, 질문, 선택지가 함께 조회된다.")
    @GetMapping("/{surveyId}")
    public Response<SurveyGetResponse> getSurvey(@JwtValidation Long memberId, @PathVariable Long surveyId) {
        SurveyGetResponse surveyGetResponse = surveyService.getSurvey(memberId, surveyId);
        return Response.success(surveyGetResponse);
    }

    @Operation(summary = "설문 삭제")
    @DeleteMapping("/{surveyId}")
    public Response<Void> deleteSurvey(@JwtValidation Long memberId, @PathVariable Long surveyId) {
        surveyService.deleteSurvey(memberId, surveyId);
        return Response.success();
    }

    // ======== 응답
    @Operation(summary = "응답 등록", description = "학생들이 응답을 등록합니다. 학생 이상만 설문이 가능합니다. 응답 id가 반환됩니다.")
    @PostMapping("/{surveyId}/replies")
    public Response<Void> getSurveyReplys(
            @JwtValidation Long memberId,
            @PathVariable Long surveyId,
            @RequestBody List<SurveyReplyCreateRequest> requests) {
        surveyService.createSurveyReply(memberId, surveyId, requests);
        return Response.success();
    }

    @Operation(summary = "설문-질문-응답조회", description = "관리자가 설문별로 보는 설문-질문-응답 조회")
    @GetMapping("/{surveyId}/replies")
    public Response<SurveyRepliesGetResponse> getSurveyReply(@JwtValidation Long memberId, @PathVariable Long surveyId) {
        SurveyRepliesGetResponse surveyReplys = surveyService.getSurveyReplies(memberId, surveyId);
        return Response.success(surveyReplys);
    }


}