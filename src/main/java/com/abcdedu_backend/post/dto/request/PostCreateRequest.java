package com.abcdedu_backend.post.dto.request;

import com.abcdedu_backend.interceptor.Member;
import com.abcdedu_backend.board.Board;
import com.abcdedu_backend.post.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateRequest {
    private String title;
    private String content;
    private Long viewCount;
    private Boolean secret;
    private Boolean commentAllow;

    private String boardName;

    public Post toEntity(Member member, Board board) {
        return Post.builder()
                .board(board)
                .member(member)
                .title(this.title)
                .content(this.content)
                .viewCount(this.viewCount)
                .secret(this.secret)
                .commentAllow(this.commentAllow)
                .build();
    }
}
