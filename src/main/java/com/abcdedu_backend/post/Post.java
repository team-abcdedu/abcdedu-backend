package com.abcdedu_backend.post;

import com.abcdedu_backend.utils.BaseTimeEntity;
import com.abcdedu_backend.board.Board;
import com.abcdedu_backend.interceptor.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "content", length = 300, nullable = false)
    private String content;

    @Column(name = "view", nullable = false)
    private Long viewCount;

    @Column(name = "secret", nullable = false)
    private Boolean secret;

    @Column(name = "comment_allow", nullable = false)
    private Boolean commentAllow;

    /*
    public static Post updatePost(PostUpdateRequest req) {
        return Post.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .secret(req.getSecret())
                .commentAllow(req.getCommentAllow())
                .build();
    }
     */



}
