package com.abcdedu_backend.post.repository;

import com.abcdedu_backend.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostReposiroty extends JpaRepository<Post, Long> {
    Optional<Post> findById (Long id);

    @EntityGraph(attributePaths = {"member", "comments"}) // TODO. member 전 컬럼을 가져오지 않도록 성능 최적화
    @Query("SELECT DISTINCT p FROM Post p")
    Page<Post> findAllWithMemberAndComment(Pageable pageable);

}
