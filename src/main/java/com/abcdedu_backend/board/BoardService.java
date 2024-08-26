package com.abcdedu_backend.board;

import com.abcdedu_backend.board.dto.request.BoardCreateRequest;
import com.abcdedu_backend.board.dto.response.BoardResponse;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardResponse findBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND));
        return BoardToBoardResponse(board);
    }

    @Transactional
    public Long addBoard(BoardCreateRequest req) {
        // 이미 존재하는 카테고리면 오류 던져짐
        checkBoardDuplicate(req);
        // 새로운 카테고리 저장
        Board board = Board.of(req.name());
        boardRepository.save(board);
        return board.getId();
    }

    private void checkBoardDuplicate(BoardCreateRequest req) {
        if (boardRepository.findByName(req.name()).isPresent()) {
            throw new ApplicationException(ErrorCode.BOARD_DUPLICATION);
        }
    }

    // ======= DTO 변환 메서드들
    // 단건 조회
    private BoardResponse BoardToBoardResponse(Board board) {
        return BoardResponse.builder()
                .name(board.getName())
                .build();
    }

}
