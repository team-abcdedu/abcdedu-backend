package com.abcdedu_backend.post.repository;

import com.abcdedu_backend.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostReposiroty extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId")
    Page<Post> findAllByBoardId(Pageable pageable, @Param("boardId") Long boardId);


}
