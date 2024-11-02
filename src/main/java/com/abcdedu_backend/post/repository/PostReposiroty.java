package com.abcdedu_backend.post.repository;

import com.abcdedu_backend.board.BoardType;
import com.abcdedu_backend.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostReposiroty extends JpaRepository<Post, Long> {

    Page<Post> findByBoardType(BoardType boardType, Pageable pageable);

}
