package com.abcdedu_backend.post;

import com.abcdedu_backend.utils.Response;
import com.abcdedu_backend.interceptor.LoginInterceptor;
import com.abcdedu_backend.interceptor.Member;
import com.abcdedu_backend.post.dto.response.PostListResponse;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import com.abcdedu_backend.post.service.PostService;
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
public class PostController {
    private final PostService postService;
    @GetMapping("/")
    public Response<List<PostListResponse>> list(@PageableDefault(sort = {"updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostListResponse> allPosts = postService.getAllPosts(pageable);
        log.info("Page<PostListResponse> allPosts = {}", allPosts.stream().count());
        return Response.success(allPosts.getContent());
    }

    @PostMapping("/")
    public Response<Long> addPost(@Valid @RequestBody PostCreateRequest req,
                                        @SessionAttribute(LoginInterceptor.LOGIN_MEMBER) Member member) {
        return Response.success(postService.createPost(req, member));
    }
}
