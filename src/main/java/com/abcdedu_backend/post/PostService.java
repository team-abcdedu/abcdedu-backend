package com.abcdedu_backend.post;
import com.abcdedu_backend.board.Board;
import com.abcdedu_backend.board.BoardRepository;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.interceptor.Member;
import com.abcdedu_backend.interceptor.MemberRole;
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

    public Page<PostListResponse> getAllPosts(Pageable pageable) {
        log.info("PostService.getAllPosts = {} ", postReposiroty.findAllWithMemberAndComment(pageable));
        return postReposiroty.findAllWithMemberAndComment(pageable)
                .map(post -> PostToPostListResponse(post));
    }

    @Transactional
    public Long createPost(PostCreateRequest req, Member member) {
        Board board = checkBoard(req.boardName());
        Post post = toEntity(member, board, req);
        postReposiroty.save(post);
        return post.getId();
    }

    public PostResponse findPost(Long postId, Member member) {
        // 해당하는 post는 없다.
        Post post = postReposiroty.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));

        // 비밀글은 본인과 관리자만 볼 수 있다.
        if (post.getSecret()) {
            if (member.getRole() != MemberRole.ADMIN && member.getId() != post.getMember().getId()) {
                throw new ApplicationException(ErrorCode.SECRET_POST_INVALID_PERMISSION);
            }
        }
        return postToPostResponse(post);
    }

    private Board checkBoard(String boardName) {
        return boardRepository.findByName(boardName)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND));
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

    public Post toEntity(Member member, Board board, PostCreateRequest req) {
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