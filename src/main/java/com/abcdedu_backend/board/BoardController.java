package com.abcdedu_backend.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    @PostMapping("/")
    public Long addBoard(@Valid @RequestBody BoardCreateRequest req) {
        return boardService.addBoard(req);
    }

    @GetMapping("/{boardId}")
    public Board findBoard(@PathVariable Long boardId) {
        return boardService.findBoard(boardId);
    }
}
