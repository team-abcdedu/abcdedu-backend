package com.abcdedu_backend.board;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND));
    }
    @Transactional
    public Long addBoard(BoardCreateRequest req) {
        // 이미 존재하는 카테고리면 오류 던져짐
        checkBoardDuplicate(req);
        // 새로운 카테고리 저장
        Board board = Board.of(req.getName());
        boardRepository.save(board);
        return board.getId();
    }

    private void checkBoardDuplicate(BoardCreateRequest req) {
        if (boardRepository.findByName(req.getName()).isPresent()) {
            throw new ApplicationException(ErrorCode.BOARD_DUPLICATION);
        }
    }
}
