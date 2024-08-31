package com.abcdedu_backend.comment;

import com.abcdedu_backend.comment.dto.request.CommentCreateRequest;
import com.abcdedu_backend.comment.dto.request.CommentUpdateRequest;
import com.abcdedu_backend.comment.dto.response.CommentResponse;
import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
@Tag(name = "댓글 기능", description = "댓글과 관련된 기능들입니다.")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping("/posts/{postId}")
    public Response<Void> create(@PathVariable Long postId, @JwtValidation Long memberId, CommentCreateRequest createRequest) {
        commentService.create(postId, memberId, createRequest);
        return Response.success();
    }

    @Operation(summary = "게시글 댓글 조회", description = "게시글 id에 따라 댓글이 조회됩니다")
    @GetMapping("/posts/{postId}")
    public Response<List<CommentResponse>> read(@PathVariable Long postId) {
        List<CommentResponse> commentResponses = commentService.read(postId);
        return Response.success(commentResponses);
    }

    @Operation(summary = "유저의 댓글 개수 조회", description = "유저 id에 따라 댓글 개수가 조회됩니다.")
    @GetMapping("/members/{memberId}")
    public Response<Long> getCommentCountByMember(@PathVariable Long memberId) {
        Long count = commentService.countCommentsByMember(memberId);
        return Response.success(count);
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping ("/{commentId}")
    public Response<Void> updateComment(@PathVariable Long commentId, @JwtValidation Long memberId, @RequestBody CommentUpdateRequest updateRequest) {
        commentService.updateComment(commentId, memberId, updateRequest);
        return Response.success();
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping ("/{commentId}")
    public Response<Void> updateComment(@PathVariable Long commentId, @JwtValidation Long memberId) {
        commentService.deleteComment(commentId,memberId);
        return Response.success();
    }
}

