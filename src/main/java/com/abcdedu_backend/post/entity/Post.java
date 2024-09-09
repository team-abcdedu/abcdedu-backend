package com.abcdedu_backend.post.entity;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import com.abcdedu_backend.post.dto.request.PostUpdateRequest;
import com.abcdedu_backend.utils.BaseTimeEntity;
import com.abcdedu_backend.board.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
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
    @Column(name = "comment_count")  // 댓글이 생성되거나 삭제될 때 마다 업데이트 해줘야 하는 필드
    private Long commentCount;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "content", length = 300, nullable = false)
    private String content;

    @Column(name = "viewCount", nullable = false)
    private Long viewCount;

    @Column(name = "secret", nullable = false)
    private Boolean secret;

    @Column(name = "comment_allow", nullable = false)
    private Boolean commentAllow;

    @Column(name = "deleted")
    private boolean deleted = false;  // 소프트 삭제 여부를 나타내는 필드

    @Column(name = "object_key")
    private String objectKey;

    public void updatePost(PostUpdateRequest request, String objectKey) {
        this.title = request.title();
        this.content = request.content();
        this.secret = request.secret();
        this.commentAllow = request.commentAllow();
        this.objectKey = objectKey;
    }


    public static Post of(Member member, Board board, PostCreateRequest req) {
        return Post.builder()
                .board(board)
                .member(member)
                .title(req.title())
                .viewCount(0L)
                .commentCount(0L)
                .content(req.content())
                .secret(req.secret())
                .commentAllow(req.commentAllow())
                .build();
    }

    public void updateObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }
    public void incrementCommentCount() {
        this.commentCount++;
    }
    public void decrementCommentCount() {
        this.commentCount--;
    }
}
