package com.abcdedu_backend.board;

import com.abcdedu_backend.board.dto.request.BoardCreateRequest;
import com.abcdedu_backend.board.dto.response.BoardResponse;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    public BoardResponse findBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND));
        return BoardToBoardResponse(board);
    }

    @Transactional
    public Long addBoard(BoardCreateRequest req, Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        checkPermission(findMember);
        checkBoardDuplicate(req);
        Board board = Board.of(req.name());
        boardRepository.save(board);
        return board.getId();
    }
    @Transactional
    public void removeBoard(Long boardId, Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        checkPermission(findMember);
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND));
        boardRepository.delete(findBoard);
    }

    // ======== 유효성 검사, 서비스 로직

    // 카테고리 수정은 관리자만 가능하다.
    private void checkPermission(Member member) {
        if (!member.getRole().equals(MemberRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.BOARD_INVALID_PERMISSION);
        }
    }
    // 이미 존재하는 카테고리면 오류 던져짐
    private void checkBoardDuplicate(BoardCreateRequest req) {
        if (boardRepository.findByName(req.name()).isPresent()) {
            throw new ApplicationException(ErrorCode.BOARD_DUPLICATION);
        }
    }

    public Board checkBoard(String boardName) {
        return boardRepository.findByName(boardName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND));
    }

    // ======= DTO 변환 메서드들
    // 단건 조회
    private BoardResponse BoardToBoardResponse(Board board) {
        return BoardResponse.builder()
                .name(board.getName())
                .build();
    }

    public List<BoardResponse> findAllBoard() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponse> boardResponses = boards.stream()
                .map(board -> new BoardResponse(board.getName()))
                .collect(Collectors.toList());
        return boardResponses;
    }


}
