package com.abcdedu_backend.post;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.post.dto.response.PostResponse;
import com.abcdedu_backend.utils.Response;
import com.abcdedu_backend.interceptor.LoginInterceptor;
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
        log.info("Page<PostListResponse> allPosts = {}", allPosts.stream().count());
        return Response.success(allPosts.getContent());
    }
    @GetMapping("/{postId}")
    public Response<PostResponse> findPost(@Valid @PathVariable Long postId,
                                           @SessionAttribute(LoginInterceptor.LOGIN_MEMBER) Member member) {
        return Response.success(postService.findPost(postId, member));
    }

    /**
     * Todo. 제목 or 작성자로 글 찾기
     */

    @PostMapping("/")
    public Response<Long> addPost(@Valid @RequestBody PostCreateRequest req,
                                        @SessionAttribute(LoginInterceptor.LOGIN_MEMBER) Member member) {
        return Response.success(postService.createPost(req, member));
    }
}
