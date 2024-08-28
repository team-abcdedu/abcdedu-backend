package com.abcdedu_backend.post;
import com.abcdedu_backend.board.Board;
import com.abcdedu_backend.board.BoardRepository;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.MemberRepository;
import com.abcdedu_backend.post.dto.response.PostListResponse;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import com.abcdedu_backend.post.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostReposiroty postReposiroty;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        return postReposiroty.findAllWithMemberAndComment(pageable)
                .map(post -> PostToPostListResponse(post));
    }

    @Transactional
    public Long createPost(PostCreateRequest req, Long memberId) {
        Board findBoard = checkBoard(req.boardName());
        Member findMember = checkMember(memberId);
        Post post = of(findMember, findBoard, req);
        postReposiroty.save(post);
        return post.getId();
    }

    public PostResponse findPost(Long postId, Long memberId) {
        Post findPost = checkPost(postId);
        Member findMember = checkMember(memberId);
        // 비밀글은 본인과 관리자만 볼 수 있다.
        if (findPost.getSecret()) {
            if (findMember.getRole() != MemberRole.ADMIN && findMember.getId() != findPost.getMember().getId()) {
                throw new ApplicationException(ErrorCode.SECRET_POST_INVALID_PERMISSION);
            }
        }
        return postToPostResponse(findPost);
    }

    // ======= 비즈니스 유효성 검사, find() =========

    private Board checkBoard(String boardName) {
        return boardRepository.findByName(boardName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND));
    }

    private Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
    }

    private Post checkPost(Long postId) {
        return postReposiroty.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    // ====== DTO, Entity 변환 =======
    // 다건 조회
    private PostListResponse PostToPostListResponse(Post post) {
        return PostListResponse.builder()
                .title(post.getTitle())
                .writer(post.getMember().getName())
                .viewCount(post.getViewCount())
                .commentCount((long) post.getComments().size())
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
                .commentCount((long) post.getComments().size())  // 댓글 수
                .comments(post.getComments())  // 댓글 리스트
                .build();
    }

    public Post of(Member member, Board board, PostCreateRequest req) {
        return Post.builder()
                .board(board)
                .member(member)
                .title(req.title())
                .viewCount(req.viewCount())
                .content(req.content())
                .secret(req.secret())
                .commentAllow(req.commentAllow())
                .build();
    }
}