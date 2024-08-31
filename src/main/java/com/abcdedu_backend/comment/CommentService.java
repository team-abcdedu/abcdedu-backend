package com.abcdedu_backend.comment;

import com.abcdedu_backend.comment.dto.request.CommentCreateRequest;
import com.abcdedu_backend.comment.dto.request.CommentUpdateRequest;
import com.abcdedu_backend.comment.dto.response.CommentResponse;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.service.MemberService;
import com.abcdedu_backend.post.Post;
import com.abcdedu_backend.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final PostService postService;

    @Transactional
    public void updateComment(Long commentId, Long memberId, CommentUpdateRequest updateRequest) {
        Comment comment = checkVaildation(commentId, memberId);
        comment.updateContent(updateRequest.content());
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = checkVaildation(commentId, memberId);
        commentRepository.deleteById(commentId);
    }


    public List<CommentResponse> read(Long postId) {
        Post findpost = postService.checkPost(postId);
        List<Comment> comments = findpost.getComments();
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> CommentResponse.builder()
                        .content(comment.getContent())
                        .writerName(comment.getMember().getName())
                        .build())
                .collect(Collectors.toList());
        return commentResponses;
    }

    @Transactional
    public void create(Long postId, Long memberId, CommentCreateRequest createRequest) {
        Post findpost = postService.checkPost(postId);
        Member findMember = memberService.checkMember(memberId);
        Comment comment = Comment.builder()
                .post(findpost)
                .member(findMember)
                .content(createRequest.content())
                //.secret(createRequest.secret())
                .build();
        commentRepository.save(comment);
    }

    public Long countCommentsByMember(Long memberId) {
        Member member = memberService.checkMember(memberId);
        return (long) member.getComments().size();
    }

    // ======== DTO 변환
    // ======= 유효성 검사
    public Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private Comment checkVaildation(Long commentId, Long memberId) {
        Member member = memberService.checkMember(memberId);
        Comment comment = checkComment(commentId);
        if (!member.getId().equals(comment.getMember().getId()) && !member.getRole().equals(MemberRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.COMMENT_INVALID_PERMISSION);
        }
        return comment;
    }
}
