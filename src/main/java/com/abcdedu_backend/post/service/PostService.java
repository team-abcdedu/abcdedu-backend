package com.abcdedu_backend.post.service;

import com.abcdedu_backend.board.Board;
import com.abcdedu_backend.board.BoardRepository;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.interceptor.Member;
import com.abcdedu_backend.post.Post;
import com.abcdedu_backend.post.PostReposiroty;
import com.abcdedu_backend.post.dto.response.PostListResponse;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostReposiroty postReposiroty;
    private final BoardRepository boardRepository;

    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        log.info("PostService.getAllPosts = {} ", postReposiroty.findAllWithMemberAndComment(pageable));
        return postReposiroty.findAllWithMemberAndComment(pageable)
                .map(post -> PostListResponse.of(post));
    }

    @Transactional
    public Long createPost(PostCreateRequest req, Member member){
        Board board = checkBoard(req.getBoardName());
        Post post = req.toEntity(member, board);
        postReposiroty.save(post);
        return post.getId();
    }

    private Board checkBoard(String boardName) {
        return boardRepository.findByName(boardName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND));
    }
}
