package com.abcdedu_backend.post;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.post.comment.CommentService;
import com.abcdedu_backend.post.comment.dto.request.CommentCreateRequest;
import com.abcdedu_backend.post.comment.dto.request.CommentUpdateRequest;
import com.abcdedu_backend.post.comment.dto.response.CommentResponse;
import com.abcdedu_backend.post.dto.response.PostResponse;
import com.abcdedu_backend.utils.Response;
import com.abcdedu_backend.post.dto.response.PostListResponse;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "게시글 기능", description = "게시글과 관련된 기능들입니다.")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/")
    @Operation(summary = "게시글 목록", description = "모든 게시글을 조회합니다.")
    public Response<List<PostListResponse>> list(@PageableDefault(sort = {"updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostListResponse> allPosts = postService.getAllPosts(pageable);
        return Response.success(allPosts.getContent());
    }
    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회", description = "특정 게시글을 조회합니다. 비밀글은 관리자와 글쓴이만 볼 수 있습니다.")
    public Response<PostResponse> findPost(@Valid @PathVariable Long postId,
                                           @JwtValidation Long memberId) {
        return Response.success(postService.findPost(postId, memberId));
    }

    @PostMapping("/")
    @Operation(summary = "게시글 추가", description = "게시글을 작성합니다.")
    public Response<Long> addPost(@Valid @RequestBody PostCreateRequest req,
                                  @JwtValidation Long memberId) {
        return Response.success(postService.createPost(req, memberId));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 작성합니다.")
    public Response<Void> deletePost(@PathVariable Long postId,
                                     @JwtValidation Long memberId) {
        postService.removePost(postId, memberId);
        return Response.success();
    }


    /**
     * Todo. 제목 or 작성자로 글 찾기
     */

    // ============ 댓글
    @Operation(summary = "댓글 생성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping("/{postId}/comments")
    public Response<Void> create(@PathVariable Long postId, @JwtValidation Long memberId, CommentCreateRequest createRequest) {
        commentService.create(postId, memberId, createRequest);
        return Response.success();
    }

    @Operation(summary = "게시글 댓글 조회", description = "게시글 id에 따라 댓글이 조회됩니다")
    @GetMapping("/{postId}/comments")
    public Response<List<CommentResponse>> read(@PathVariable Long postId) {
        List<CommentResponse> commentResponses = commentService.read(postId);
        return Response.success(commentResponses);
    }

    @Operation(summary = "유저의 댓글 개수 조회", description = "유저 id에 따라 댓글 개수가 조회됩니다.")
    @GetMapping("/comments/{memberId}")
    public Response<Long> getCommentCountByMember(@PathVariable Long memberId) {
        Long count = commentService.countCommentsByMember(memberId);
        return Response.success(count);
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping ("/comments/{commentId}")
    public Response<Void> updateComment(@PathVariable Long commentId, @JwtValidation Long memberId, @RequestBody CommentUpdateRequest updateRequest) {
        commentService.updateComment(commentId, memberId, updateRequest);
        return Response.success();
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping ("/comments/{commentId}")
    public Response<Void> updateComment(@PathVariable Long commentId, @JwtValidation Long memberId) {
        commentService.deleteComment(commentId,memberId);
        return Response.success();
    }

}
