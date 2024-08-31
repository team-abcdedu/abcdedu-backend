package com.abcdedu_backend.post;

import com.abcdedu_backend.common.jwt.JwtValidation;
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

}
