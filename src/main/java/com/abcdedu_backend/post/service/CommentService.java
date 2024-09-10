package com.abcdedu_backend.post.service;

import com.abcdedu_backend.post.dto.request.CommentCreateRequest;
import com.abcdedu_backend.post.dto.request.CommentUpdateRequest;
import com.abcdedu_backend.post.dto.response.CommentResponse;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.service.MemberService;
import com.abcdedu_backend.post.entity.Comment;
import com.abcdedu_backend.post.entity.Post;
import com.abcdedu_backend.post.repository.CommentRepository;
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
    public void deleteComment(Long postId, Long commentId, Long memberId) {
        Comment findComment = checkVaildation(commentId, memberId);
        postService.checkPost(postId).decrementCommentCount();
        commentRepository.deleteById(commentId);
    }


    public List<CommentResponse> readComments(Long postId) {
        Post findpost = postService.checkPost(postId);
        CheckPostAllowedComment(findpost);
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
    public void CreateComment(Long postId, Long memberId, CommentCreateRequest createRequest) {
        Post findpost = postService.checkPost(postId);
        CheckPostAllowedComment(findpost);
        findpost.incrementCommentCount();
        Member findMember = memberService.checkMember(memberId);
        Comment comment = Comment.builder()
                .post(findpost)
                .member(findMember)
                .content(createRequest.content())
                .build();
        commentRepository.save(comment);
    }

    public Long countCommentsByMember(Long memberId) {
        Member member = memberService.checkMember(memberId);
        return (long) member.getComments().size();
    }

    private void CheckPostAllowedComment(Post post) {
        if (!post.getCommentAllow()) {
            throw new ApplicationException(ErrorCode.POST_NOT_ALLOWED_COMMENT);
        }
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
            throw new ApplicationException(ErrorCode.ADMIN_OR_WRITER_PERMISSION);
        }
        return comment;
    }
}