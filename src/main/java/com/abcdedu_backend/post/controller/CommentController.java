package com.abcdedu_backend.post.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.post.dto.request.CommentUpdateRequest;
import com.abcdedu_backend.post.service.CommentService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "댓글 기능", description = "게시글 댓글에 관련된 기능들입니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),

})
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "유저의 댓글 개수 조회", description = "유저 id에 따라 댓글 개수가 조회됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.", content = @Content)
    })
    @GetMapping("/members/{memberId}")
    public Response<Long> getCommentCountByMember(@PathVariable Long memberId) {
        Long count = commentService.countCommentsByMember(memberId);
        return Response.success(count);
    }

    @Operation(summary = "댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 사용자/댓글을 찾을 수 없습니다.", content = @Content),
            @ApiResponse(responseCode = "401", description = "해당 기능은 관리자/작성자만 사용가능합니다.", content = @Content)
    })
    @PatchMapping("/{commentId}")
    public Response<Void> updateComment(@PathVariable Long commentId, @JwtValidation Long memberId, @RequestBody CommentUpdateRequest updateRequest) {
        commentService.updateComment(commentId, memberId, updateRequest);
        return Response.success();
    }

}
