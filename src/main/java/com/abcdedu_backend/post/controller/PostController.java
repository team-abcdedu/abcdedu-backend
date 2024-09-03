package com.abcdedu_backend.post.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.post.service.CommentService;
import com.abcdedu_backend.post.dto.request.CommentCreateRequest;
import com.abcdedu_backend.post.dto.response.CommentResponse;
import com.abcdedu_backend.post.dto.response.PostResponse;
import com.abcdedu_backend.post.service.PostService;
import com.abcdedu_backend.utils.Response;
import com.abcdedu_backend.post.dto.response.PostListResponse;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "공통 게시글 기능", description = "게시글과 관련된 기능들입니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
public class PostController {
    private final PostService postService;
    private final CommentService commentService;


    @GetMapping("/")
    @Operation(summary = "게시글 목록", description = "모든 게시글을 조회합니다.")
    public Response<List<PostListResponse>> readAllPost() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        List<PostListResponse> allPosts = postService.getAllPosts(pageable);
        return Response.success(allPosts);
    }


    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회", description = "특정 게시글을 조회합니다. 비밀글은 관리자와 글쓴이만 볼 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 포스트가 없습니다.", content = @Content),
            @ApiResponse(responseCode = "401", description = "본인과 관리자만 가능한 기능입니다.", content = @Content),
    })
    public Response<PostResponse> findPost(@Valid @PathVariable Long postId,
                                           @JwtValidation Long memberId) {
        return Response.success(postService.findPost(postId, memberId));
    }

    @PostMapping("/")
    @Operation(summary = "게시글 추가", description = "게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "공백 불가 : 제목", content = @Content)
    })
    public Response<Long> addPost(@Valid @RequestBody PostCreateRequest req,
                                  @JwtValidation Long memberId) {
        return Response.success(postService.createPost(req, memberId,""));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 포스트가 없습니다.", content = @Content),
            @ApiResponse(responseCode = "401", description = "본인과 관리자만 가능한 기능입니다.", content = @Content),
    })
    public Response<Void> deletePost(@PathVariable Long postId,
                                     @JwtValidation Long memberId) {
        postService.removePost(postId, memberId);
        return Response.success();
    }


    /**
     * Todo. 제목 or 작성자로 글 찾기
     */

    // ============ 댓글
    @Operation(summary = "게시글에 댓글 생성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping("/{postId}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 포스트/멤버가 없습니다.", content = @Content)
    })
    public Response<Void> create(@PathVariable Long postId, @JwtValidation Long memberId, CommentCreateRequest createRequest) {
        commentService.CreateComment(postId, memberId, createRequest);
        return Response.success();
    }

    @Operation(summary = "특정 게시글 댓글 목록 조회", description = "게시글 id에 따라 댓글이 조회됩니다")
    @GetMapping("/{postId}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 포스트가 없습니다.", content = @Content)
    })
    public Response<List<CommentResponse>> read(@PathVariable Long postId) {
        List<CommentResponse> commentResponses = commentService.readComments(postId);
        return Response.success(commentResponses);
    }

    @Operation(summary = "특정 게시글 특정 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 사용자/댓글을 찾을 수 없습니다.", content = @Content),
            @ApiResponse(responseCode = "401", description = "해당 기능은 관리자/작성자만 사용가능합니다.", content = @Content)
    })
    @DeleteMapping("/{postId}/{commentId}")
    public Response<Void> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @JwtValidation Long memberId) {
        commentService.deleteComment(postId, commentId, memberId);
        return Response.success();
    }

}
