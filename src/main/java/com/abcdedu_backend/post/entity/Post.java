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

    @Column(name = "comment_count")  // 댓글이 생성되거나 삭제될 때 마다 업데이트 해줘야 하는 필드
    private Long commentCount;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "viewCount", nullable = false)
    private Long viewCount;

    @Column(name = "secret", nullable = false)
    private Boolean secret;

    @Column(name = "comment_allow", nullable = false)
    private Boolean commentAllow;

    @Column(name = "deleted")
    private boolean deleted = false;  // 소프트 삭제 여부를 나타내는 필드

    @Column(name = "file_url")
    private String fileUrl;

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

    public void updateFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    public void incrementCommentCount() {
        this.commentCount++;
    }
    public void decrementCommentCount() {
        this.commentCount--;
    }
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void changeBoard(Board board) {
        board.getPosts().add(this);
        this.board = board;
    }

    public void update(PostUpdateRequest updateReq) {
        this.title = updateReq.title();
        this.content = updateReq.content();
        this.secret = updateReq.secret();
        this.commentAllow = updateReq.commentAllow();
    }
}
