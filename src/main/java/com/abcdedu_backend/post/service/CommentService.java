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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final PostService postService;

    @Transactional
    public Long createComment(Long postId, Long memberId, CommentCreateRequest createRequest) {
        Member findMember = memberService.checkMember(memberId);
        Post findpost = postService.checkPost(postId);
        CheckPostAllowedComment(findpost);

        Comment comment = Comment.builder()
                .member(findMember)
                .post(findpost)
                .content(createRequest.content())
                .build();
        commentRepository.save(comment);
        findpost.incrementCommentCount();
        return comment.getId();
    }


    public Page<CommentResponse> readComments(Long postId, Pageable pageable) {
        Post findpost = postService.checkPost(postId);
        CheckPostAllowedComment(findpost);
        Page<Comment> comments = commentRepository.findByPost(findpost, pageable);
        return comments
                .map(comment -> CommentResponse.builder()
                        .commentId(comment.getId())
                        .content(comment.getContent())
                        .writerName(comment.getMember().getName())
                        .writerEmail(comment.getMember().getEmail())
                        .createdAt(comment.getCreatedAt())
                        .build());
    }

    @Transactional
    public void updateComment(Long commentId, Long memberId, CommentUpdateRequest updateRequest) {
        Comment comment = checkVaildation(commentId, memberId);
        comment.updateContent(updateRequest.content());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long memberId) {
        checkVaildation(commentId, memberId);
        postService.checkPost(postId).decrementCommentCount();
        commentRepository.deleteById(commentId);
    }



    public int countCommentsByMember(Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        List<Comment> findComments = commentRepository.findAllByMember(findMember);
        return findComments.size();
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
