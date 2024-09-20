package com.abcdedu_backend.post.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.PageManager;
import com.abcdedu_backend.common.request.PagingRequest;
import com.abcdedu_backend.common.request.SortRequest;
import com.abcdedu_backend.common.response.PagedResponse;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.post.dto.request.PostUpdateRequest;
import com.abcdedu_backend.post.service.CommentService;
import com.abcdedu_backend.post.dto.request.CommentCreateRequest;
import com.abcdedu_backend.post.dto.response.CommentResponse;
import com.abcdedu_backend.post.dto.response.PostResponse;
import com.abcdedu_backend.post.service.PostService;
import com.abcdedu_backend.utils.Response;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "게시글 기능", description = "게시글과 관련된 기능들입니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
public class PostController {
    private final PostService postService;
    private final CommentService commentService;


    @GetMapping("/{postId}")
    @Operation(summary = "특정 게시글 조회", description = "특정 게시글을 조회합니다. 비밀글은 관리자와 글쓴이만 볼 수 있습니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "404", description = "해당 포스트가 없습니다.", content = @Content),
//            @ApiResponse(responseCode = "403", description = "본인과 관리자만 가능한 기능입니다.", content = @Content),
//    })
    public Response<PostResponse> readPost(@Valid @PathVariable Long postId,
                                           @JwtValidation Long memberId) {
        return Response.success(postService.getPost(postId, memberId));
    }

    @PostMapping()
    @Operation(summary = "게시글 생성", description = "게시글을 작성합니다. 역할이 학생이상이여야만 작성이 가능합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "공백 요청 불가 : 제목", content = @Content),
//            @ApiResponse(responseCode = "403", description = "학생 등급 이하가 기능을 요청할 때 발생합니다.", content = @Content)
//    })
    public Response<Long> createPost(@Valid @ModelAttribute PostCreateRequest req,
                                     BindingResult bindingResult,
                                     @RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                     @JwtValidation Long memberId) {
        if (bindingResult.hasErrors()) throw new ApplicationException(ErrorCode.INVALID_REQUEST);
        return Response.success(postService.createPost(req, memberId, multipartFile));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. 작성자 본인과 관리자만 삭제가 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 포스트가 없습니다.", content = @Content),
            @ApiResponse(responseCode = "403", description = "본인과 관리자만 가능한 기능입니다.", content = @Content),
    })
    public Response<Void> deletePost(@PathVariable Long postId, @JwtValidation Long memberId) {
        postService.removePost(postId, memberId);
        return Response.success();
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다..")
    /*
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 포스트가 없습니다.", content = @Content),
            @ApiResponse(responseCode = "403", description = "본인과 관리자만 가능한 기능입니다.", content = @Content),
    })
     */
    public Response<Long> updatePost(@PathVariable Long postId,
                                     @Valid @ModelAttribute PostUpdateRequest postUpdateRequest,
                                     BindingResult bindingResult,
                                     @RequestPart(value = "file", required = false) MultipartFile multipartFile,
                                     @JwtValidation Long memberId) {
        if (bindingResult.hasErrors()) throw new ApplicationException(ErrorCode.INVALID_REQUEST);
        return Response.success(postService.updatePost(postId, memberId,postUpdateRequest, multipartFile));
    }


    // ============ 댓글
    @Operation(summary = "게시글에 댓글 생성", description = "게시글에 댓글을 작성합니다. content는 Not null입니다.")
    @PostMapping("/{postId}/comments")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "404", description = "해당 포스트/멤버가 없습니다.", content = @Content),
//            @ApiResponse(responseCode = "403", description = "댓글 불가 게시글입니다.", content = @Content)
//    })
    public Response<Long> createComment(@PathVariable Long postId, @JwtValidation Long memberId,
                                        @Valid @RequestBody CommentCreateRequest createRequest) {
        Long commentId = commentService.createComment(postId, memberId, createRequest);
        return Response.success(commentId);
    }

    @Operation(summary = "게시글 댓글 목록 조회", description = "게시글 id에 따라 댓글이 조회됩니다")
    @GetMapping("/{postId}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 포스트가 없습니다.", content = @Content),
            @ApiResponse(responseCode = "403", description = "댓글 불가 게시글입니다.", content = @Content)
    })
    public Response<PagedResponse<CommentResponse>> readComment(@PathVariable Long postId, PagingRequest pagingRequest, SortRequest sortRequest) {
        Page<CommentResponse> commentResponses = commentService.readComments(postId,new PageManager(pagingRequest, sortRequest).makePageRequest());
        return Response.success(PagedResponse.from(commentResponses));
    }

    @Operation(summary = "게시글 특정 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "해당 사용자/댓글을 찾을 수 없습니다.", content = @Content),
            @ApiResponse(responseCode = "403", description = "해당 기능은 관리자/작성자만 사용가능합니다.", content = @Content)
    })
    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @JwtValidation Long memberId) {
        commentService.deleteComment(postId, commentId, memberId);
        return Response.success();
    }

    /**
     * 등업 기능
     */
    @Operation(summary = "게시글 작성자의 등급을 선택한 등급으로 올린다.", description = "관리자 이상만 가능한 기능입니다. 등업게시판에서만 사용합니다.")
    @PostMapping("/{postId}/levelup/{roleName}")
    public Response<Void> levelUpPostWriter(@JwtValidation Long memberId,
                                            @PathVariable Long postId,
                                            @PathVariable MemberRole roleName) {
        postService.levelUpPostWriter(memberId, postId, roleName);
        return Response.success();
    }

}
