package com.abcdedu_backend.post.service;
import com.abcdedu_backend.board.Board;
import com.abcdedu_backend.board.BoardService;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.service.MemberService;
import com.abcdedu_backend.post.dto.request.PostUpdateRequest;
import com.abcdedu_backend.post.dto.response.PostListResponse;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import com.abcdedu_backend.post.dto.response.PostResponse;
import com.abcdedu_backend.post.entity.Post;
import com.abcdedu_backend.post.repository.PostReposiroty;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostReposiroty postReposiroty;
    private final BoardService boardService;
    private final MemberService memberService;


    public List<PostListResponse> readPostList(Long boardId, Pageable pageable) {
        Page<Post> findPostList = postReposiroty.findAllByBoardId(boardId, pageable);
        return findPostList.stream()
                .map(post -> PostToPostListResponse(post))
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long postId, Long memberId) {
        Post findPost = checkPost(postId);
        Member findMember = memberService.checkMember(memberId);
        checkPermission(findMember, findPost);
        return postToPostResponse(findPost);
    }

    @Transactional
    public Long createPost(PostCreateRequest req, Long memberId) {
        Board findBoard = boardService.checkBoard(req.boardId());
        Member findMember = memberService.checkMember(memberId);
        if (hasPostingRestrictedByRole(findBoard)) checkMemberGradeHigherThanBasic(findMember);
        Post post = of(findMember, findBoard, req);
        postReposiroty.save(post);
        boardService.addPostToBoard(findBoard, post);
        return post.getId();
    }


    @Transactional
    public void removePost(Long postId, Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        Post findPost = checkPost(postId);
        checkPermission(findMember, findPost);
        postReposiroty.delete(findPost);
    }

    @Transactional
    public Long updatePost(Long postId, Long memberId, PostUpdateRequest updateRequest) {
        Member findMember = memberService.checkMember(memberId);
        Post findPost = checkPost(postId);
        checkPermission(findMember, findPost);
        findPost.updatePost(updateRequest);
        postReposiroty.save(findPost);
        return findPost.getId();
    }

    public Post checkPost(Long postId) {
        return postReposiroty.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    // post 게시자 본인과 관리자만 할 수 있는 기능에 추가
    private void checkPermission(Member member, Post post) {
        if (!member.getRole().equals(MemberRole.ADMIN) && !member.getId().equals(post.getMember().getId())) {
            throw new ApplicationException(ErrorCode.POST_INVALID_PERMISSION);
        }
    }
    // role이 학생 이상인지
    private void checkMemberGradeHigherThanBasic(Member member) {
        if (!member.isStudent() && !member.isAdmin()) {
            throw new ApplicationException(ErrorCode.ROLE_INVALID_PERMISSION);
        }
    }
    private boolean hasPostingRestrictedByRole(Board board) {
        return !boardIdToName(board.getId()).equals("rating");
    }

    private String boardIdToName(Long boardId) {
        return boardService.boardIdToName(boardId);
    }
    // ====== DTO, Entity 변환 =======
    // 다건 조회
    private PostListResponse PostToPostListResponse(Post post) {
        return PostListResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .writer(post.getMember().getName())
                        .viewCount(post.getViewCount())
                        .commentCount(post.getCommentCount())
                        .updatedAt(post.getUpdatedAt())
                        .build();
    }

    // 단건 조회
    private PostResponse postToPostResponse(Post post) {
        return PostResponse.builder()
                .title(post.getTitle())
                .writer(post.getMember().getName())  // writer는 member의 이름으로 설정
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())  // 댓글 수
                .build();
    }

    public Post of(Member member, Board board, PostCreateRequest req) {
        return Post.builder()
                .board(board)
                .member(member)
                .title(req.title())
                .viewCount(req.viewCount())
                .commentCount(req.commentCount())
                .content(req.content())
                .secret(req.secret())
                .commentAllow(req.commentAllow())
                .build();
    }


}