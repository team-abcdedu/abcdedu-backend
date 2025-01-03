package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.homework.dto.response.HomeworkGetRes;
import com.abcdedu_backend.homework.dto.request.HomeworkReplyCreateReq;
import com.abcdedu_backend.homework.service.HomeworkService;
import com.abcdedu_backend.homework.service.ReadRepresentativeService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/homeworks")
@RequiredArgsConstructor
@Tag(name = "공통과제 기능", description = "조회, 응답 (학생 이상 가능)")
public class HomeworkController {

    private final HomeworkService homeworkService;
    private final ReadRepresentativeService readRepresentativeService;

    @Operation(summary = "공통 과제 상세 조회", description = "응답을 하기 위해 문제 내용이 조회된다.")
    @GetMapping
    public Response<HomeworkGetRes> getHomework(@JwtValidation Long memberId) {
        Long representativeHomeworkId = readRepresentativeService.readRepresentativeHomework();
        HomeworkGetRes res = homeworkService.getHomework(memberId, representativeHomeworkId);
        return Response.success(res);
    }

    @Operation(summary = "과제 응답 제출", description = "과제 응답을 제출합니다. 응답은 list로 들어가기 때문에 질문 갯수에 맞춰서 List로 요청바랍니다.")
    @PostMapping("/{homeworkId}/replies")
    public Response<Void> createHomeworkReply(@JwtValidation Long memberId, @PathVariable Long homeworkId,
                                              @Valid @RequestBody List<HomeworkReplyCreateReq> replyRequests) {
        homeworkService.createHomeworkReply(memberId, homeworkId, replyRequests);
        return Response.success();
    }

}
