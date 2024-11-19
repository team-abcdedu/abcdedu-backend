package com.abcdedu_backend.post.repository;

import com.abcdedu_backend.member.adapter.out.entity.MemberEntity;
import com.abcdedu_backend.post.entity.Comment;
import com.abcdedu_backend.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPost(Post post, Pageable pageable);

    List<Comment> findAllByMember(MemberEntity member);
}
