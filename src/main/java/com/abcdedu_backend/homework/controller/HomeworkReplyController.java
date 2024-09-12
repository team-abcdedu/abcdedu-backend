package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.global.security.LoginUserDetails;
import com.abcdedu_backend.homework.dto.HomeworkReplyReq;
import com.abcdedu_backend.homework.dto.HomeworkReplyRes;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/homeworks")
@Tag(name = "공통 과제 제출", description = "공통 과제 제출 관련 API")
public class HomeworkReplyController {

    @Operation(summary = "사용자 과제 답안 조회", description = "사용자 자신이 제출한 과제 답안 기록을 조회합니다.")
    @GetMapping("/{homeworkId}/replies/me")
    public Response<List<HomeworkReplyRes.UserReplyModel>> getHomeworkUserReplies(
        @PathVariable Long homeworkId,
        @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        throw new UnsupportedOperationException();
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{homeworkId}/replies")
    @Operation(summary = "과제 답안 제출 UPSERT", description = "과제 답안을 [제출 혹은 수정] 합니다.")
    public Response<Void> createHomeworkReplies(
        @PathVariable Long homeworkId,
        @RequestBody HomeworkReplyReq.UpsertMany req
    ) {
        throw new UnsupportedOperationException();
    }
}
