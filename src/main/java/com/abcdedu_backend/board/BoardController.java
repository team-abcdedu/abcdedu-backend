package com.abcdedu_backend.board;

import com.abcdedu_backend.board.dto.request.BoardCreateRequest;
import com.abcdedu_backend.board.dto.response.BoardResponse;
import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.common.page.request.PagingRequest;
import com.abcdedu_backend.common.page.PageManager;
import com.abcdedu_backend.common.page.request.SortRequest;
import com.abcdedu_backend.common.page.response.PagedResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Tag(name = "게시판 카테고리", description = "게시판 카테고리 추가/조회/삭제하는 기능입니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다.", content = @Content),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "401", description = "본인과 관리자만 가능한 기능입니다.", content = @Content),
        @ApiResponse(responseCode = "404", description = "해당 카테고리를 찾을 수 없습니다.", content = @Content),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 카테고리입니다.", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;

    @Operation(summary = "게시판 카테고리 추가", description = "카테고리를 추가합니다. 관리자 이상만 가능합니다.")
    @PostMapping()
    public Response<Long> addBoard(@Valid @RequestBody BoardCreateRequest req, @JwtValidation Long memberId) {
        return Response.success(boardService.addBoard(req, memberId));
    }

    @Operation(summary = "게시판 카테고리 삭제", description = "카테고리를 삭제합니다. 관리자 이상만 가능합니다.")
    @DeleteMapping("/{boardId}")
    public Response<Void> deleteBoard(@PathVariable Long boardId, @JwtValidation Long memberId) {
        boardService.removeBoard(boardId,memberId);
        return Response.success();
    }

    @Operation(summary = "모든 게시판 카테고리 조회", description = "카테고리를 모두 조회합니다.")
    @GetMapping()
    public Response<List<BoardResponse>> findAllBoard() {
        return Response.success(boardService.findAllBoard());
    }


    @GetMapping("/{boardName}/posts")
    @Operation(summary = "카테고리별 게시글 목록", description = "게시글 목록을 카테고리별로 조회합니다. 로그인 안 한 사람도 볼 수 있습니다." +
            "boardName은 대소문자 상관 없이 이름만 맞으면 조회 가능합니다.")
    public Response<PagedResponse<PostListResponse>> getPostListV2(@PathVariable String boardName, PagingRequest pagingRequest, SortRequest sortRequest) {
        Page<PostListResponse> allPosts = postService.getPostsV2(boardName, new PageManager(pagingRequest, sortRequest).makePageRequest());
        return Response.success(PagedResponse.from(allPosts));
    }

}
