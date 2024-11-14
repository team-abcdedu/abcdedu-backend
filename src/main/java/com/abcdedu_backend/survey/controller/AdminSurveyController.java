package com.abcdedu_backend.survey.controller;


import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.survey.dto.response.SurveyRepliesGetResponse;
import com.abcdedu_backend.survey.service.SurveyService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/surveys")
@RequiredArgsConstructor
@Tag(name = "관리자 기능")
public class AdminSurveyController {

    private final SurveyService surveyService;

    @Operation(summary = "설문-질문-응답조회", description = "관리자가 설문별로 보는 설문-질문-응답 조회")
    @GetMapping("/{surveyId}/replies")
    public Response<SurveyRepliesGetResponse> getSurveyReply(@JwtValidation Long memberId, @PathVariable Long surveyId) {
        SurveyRepliesGetResponse surveyReplys = surveyService.getSurveyReplies(memberId, surveyId);
        return Response.success(surveyReplys);
    }
}
