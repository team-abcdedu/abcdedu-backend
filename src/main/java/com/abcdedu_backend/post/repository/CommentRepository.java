package com.abcdedu_backend.post.repository;

import com.abcdedu_backend.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
