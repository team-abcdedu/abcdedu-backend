package com.abcdedu_backend.post.controller;

import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import com.abcdedu_backend.post.dto.response.PostListResponse;
import com.abcdedu_backend.post.service.PostService;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frees")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "자유 게시판")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
public class FreePostController {
    private final PostService postService;
    @GetMapping("/")
    @Operation(summary = "자유 게시글 목록", description = "자유 게시글을 조회합니다.")
    public Response<List<PostListResponse>> readAllFreePost() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        List<PostListResponse> allPosts = postService.getAllPosts(pageable, "free");
        return Response.success(allPosts);
    }


    @PostMapping("/")
    @Operation(summary = "게시글 추가", description = "게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "공백 불가 : 제목", content = @Content)
    })
    public Response<Long> addPost(@Valid @RequestBody PostCreateRequest req,
                                  @JwtValidation Long memberId) {
        return Response.success(postService.createPost(req, memberId,"free"));
    }
}
