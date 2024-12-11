package com.abcdedu_backend.survey.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.PageManager;
import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.request.SortRequest;
import com.abcdedu_backend.common.page.response.PagedResponse;
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
@Tag(name = "설문 기능", description = "일반사용자가 대표 설문에 대해 응답을 등록하는 api 입니다.")
public class SurveyController {

    private final SurveyService surveyService;

    @Operation(summary = "설문 조회", description = "답변을 하기 위해 설문을 조회한다.")
    @GetMapping("/{surveyId}")
    public Response<SurveyGetResponse> getSurvey(@JwtValidation Long memberId, @PathVariable Long surveyId) {
        SurveyGetResponse surveyGetResponse = surveyService.getSurvey(memberId, surveyId);
        return Response.success(surveyGetResponse);
    }

    @Operation(summary = "응답 등록", description = "학생들이 응답을 등록합니다. 학생 이상만 설문이 가능합니다. 응답 id가 반환됩니다.")
    @PostMapping("/{surveyId}/replies")
    public Response<Void> getSurveyReplys(
            @JwtValidation Long memberId,
            @PathVariable Long surveyId,
            @RequestBody List<SurveyReplyCreateRequest> requests) {
        surveyService.createSurveyReply(memberId, surveyId, requests);
        return Response.success();
    }

}