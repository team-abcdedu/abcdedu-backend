package com.abcdedu_backend.board;

import com.abcdedu_backend.board.dto.request.BoardCreateRequest;
import com.abcdedu_backend.board.dto.response.BoardResponse;
import com.abcdedu_backend.common.jwt.JwtValidation;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Tag(name = "게시판 카테고리", description = "게시판 카테고리 추가/조회/삭제하는 기능입니다.")
public class BoardController {

    private final BoardService boardService;
    @Operation(summary = "게시판 카테고리 추가", description = "카테고리를 추가합니다. 관리자 이상만 가능합니다.")
    @PostMapping("/")
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
    @GetMapping("/")
    public Response<List<BoardResponse>> findAllBoard() {
        return Response.success(boardService.findAllBoard());
    }

    @Operation(summary = "게시판 카테고리 조회", description = "카테고리 ID를 통해 개별 조회합니다.")
    @GetMapping("/{boardId}")
    public Response<BoardResponse> findBoard(@PathVariable Long boardId) {
        return Response.success(boardService.findBoard(boardId));
    }

}
