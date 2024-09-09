package com.abcdedu_backend.survey.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.survey.dto.request.SurveyChoiceCreateRequest;
import com.abcdedu_backend.survey.dto.request.SurveyCreateRequest;
import com.abcdedu_backend.survey.dto.request.SurveyQuestionCreateRequest;
import com.abcdedu_backend.survey.dto.request.SurveyReplyCreateRequest;
import com.abcdedu_backend.survey.dto.response.*;
import com.abcdedu_backend.survey.entity.SurveyQuestion;
import com.abcdedu_backend.survey.entity.SurveyQuestionChoice;
import com.abcdedu_backend.survey.entity.SurveyReply;
import com.abcdedu_backend.survey.service.SurveyService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.", content = @Content),
        @ApiResponse(responseCode = "403", description = "api 권한이 없습니다. (admin만 가능)", content = @Content),
        @ApiResponse(responseCode = "404", description = "~를 찾을 찾을 수 없습니다. (설문, 질문, 응답 등)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
@Tag(name = "설문 기능", description = "설문 관련 api 입니다. 응답 등록 외 모든 기능 관리자만 사용가능 합니다.")
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
    public Response<List<SurveyListResponse>> getSurveys(@JwtValidation Long memberId) {
        List<SurveyListResponse> surveys = surveyService.getSurveys(memberId);
        return Response.success(surveys);
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

    // ======= 질문
    @Operation(summary = "질문 등록", description = "설문에 질문을 생성합니다. type은 객관식, 서술형 중에 골라서 입력해주세요.")
    @PostMapping("/{surveyId}/questions")
    public Response<Long> createSurveyQuestion(
            @JwtValidation Long memberId,
            @PathVariable Long surveyId,
            @RequestBody SurveyQuestionCreateRequest request) {
        SurveyQuestion question = surveyService.createQuestion(memberId, surveyId, request);
        return Response.success(question.getId());
    }


    // ======== 질문 선택지
    @Operation(summary = "객관식 질문 선택지 조회", description = "서술형 질문은 조회 할 수 없습니다.")
    @GetMapping("/{surveyId}/questions/{questionId}/choices")
    public Response<List<SurveyQuestionChoiceGetResponse>> getSurveyQuestionChoices(
            @PathVariable Long surveyId,
            @PathVariable Long questionId) {
        List<SurveyQuestionChoiceGetResponse> choices = surveyService.getchoices(surveyId, questionId);
        return Response.success(choices);
    }
    @Operation(summary = "질문 선택지 등록", description = "질문에 포함 될 객관식 선택지를 생성합니다. 선택지는 한번에 여러개를 등록하며, 등록 후에는 해당 선택지를 가지게 되는 질문의 id를 다시 반환합니다.")
    @PostMapping("/{surveyId}/questions/{questionId}/choices")
    public Response<Long> createSurveyQeustionChoice(
            @JwtValidation Long memberId,
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            @RequestBody List<SurveyChoiceCreateRequest> requests) {
        List<SurveyQuestionChoice> choices = surveyService.createChoice(memberId, surveyId, questionId, requests);
        return Response.success(questionId); // 해당 선택지를 조회할 수 있는 질문 Id로 리턴
    }

    // ======== 응답
    @Operation(summary = "응답 등록", description = "학생들이 응답을 등록합니다. 학생 이상만 설문이 가능합니다. 응답 id가 반환됩니다.")
    @PostMapping("/{surveyId}/questions/{questionId}/replies")
    public Response<Long> getSurveyReplys(
            @JwtValidation Long memberId,
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            @RequestBody SurveyReplyCreateRequest request) {
        SurveyReply reply = surveyService.createSurveyReply(memberId, surveyId, questionId, request);
        return Response.success(reply.getId());
    }

    @Operation(summary = "설문-질문-응답조회", description = "관리자가 설문별로 보는 설문-질문-응답 조회")
    @GetMapping("/replies")
    public Response<SurveyReplyGetResponse> getSurveyReply(@JwtValidation Long memberId) {
        surveyService.getSurveyReply(memberId);
        return Response.success();
    }


}