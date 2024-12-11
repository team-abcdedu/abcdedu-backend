package com.abcdedu_backend.post.repository;

import com.abcdedu_backend.board.Board;
import com.abcdedu_backend.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface PostReposiroty extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.id = :postId")
    Optional<Post> findById(@Param("postId") Long postId);
//    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId")
    Page<Post> findAllByBoardId(@Param("boardId") Long boardId, Pageable pageable);


//    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT p FROM Post p WHERE p.board.name = :boardName")
    Page<Post> findAllByBoardName(@Param("boardName") String boardName, Pageable pageable);

    // 이전 글 조회 (해당 게시판의 이전 글)
    Optional<Post> findFirstByIdLessThanAndBoardOrderByIdDesc(Long postId, Board board);

    // 다음 글 조회 (해당 게시판의 다음 글)
    Optional<Post> findFirstByIdGreaterThanAndBoardOrderByIdAsc(Long postId, Board board);

}
