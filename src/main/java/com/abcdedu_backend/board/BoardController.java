package com.abcdedu_backend.board;

import com.abcdedu_backend.board.dto.request.BoardCreateRequest;
import com.abcdedu_backend.board.dto.response.BoardResponse;
import com.abcdedu_backend.utils.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    @PostMapping("/")
    public Response<Long> addBoard(@Valid @RequestBody BoardCreateRequest req) {
        return Response.success(boardService.addBoard(req));
    }

    @GetMapping("/{boardId}")
    public Response<BoardResponse> findBoard(@PathVariable Long boardId) {
        return Response.success(boardService.findBoard(boardId));
    }
}
