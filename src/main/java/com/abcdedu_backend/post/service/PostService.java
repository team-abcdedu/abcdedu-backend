package com.abcdedu_backend.post.service;
import com.abcdedu_backend.board.Board;
import com.abcdedu_backend.board.BoardService;
import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
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
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostReposiroty postReposiroty;
    private final BoardService boardService;
    private final MemberService memberService;
    private final FileHandler fileHandler;


    public Page<PostListResponse> getPosts(Long boardId, Pageable pageable) {
        Page<Post> findPostList = postReposiroty.findAllByBoardId(boardId, pageable); // TODO. REVIEW. findByBoard 방식과 findByBoardId 방식 중 어느것이 나을지 고민
        return findPostList.map(this::postToPostListResponse);
    }

    public PostResponse getPost(Long postId, Long memberId) {
        Post post = checkPost(postId);
        Member member = memberService.checkMember(memberId);
        if (post.getSecret()) {
            checkPermission(member, post);

        }

        return postToPostResponse(post);
    }

    @Transactional
    public Long createPost(PostCreateRequest req, Long memberId, MultipartFile file) {
        Board findBoard = boardService.checkBoard(req.boardId());
        Member findMember = memberService.checkMember(memberId);
        if (hasPostingRestrictedByRole(findBoard)) checkMemberGradeHigherThanBasic(findMember);
        // 게시글 저장
        Post post = Post.of(findMember, findBoard, req);
        post.changeBoard(findBoard);
        postReposiroty.save(post);
        // 파일
        if (hasFile(file)) post.updateFileUrl(fileHandler.upload(file, FileDirectory.POST_ATTACHMENT, post.getId().toString()));
        else post.updateFileUrl(""); // NULL을 방지하기 위한 공백 삽입
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
    public Long updatePost(Long postId, Long memberId, PostUpdateRequest updateRequest, MultipartFile file) {
        Member findMember = memberService.checkMember(memberId);
        Post post = checkPost(postId);
        checkPermission(findMember, post);
        // 게시글 수정
        post.update(updateRequest);
        //파일
        if (hasFile(file)) post.updateFileUrl(fileHandler.upload(file, FileDirectory.POST_ATTACHMENT, postId.toString()));
        return post.getId();
    }

    public void deletePostFile(Long postId, Long memberId) {
        Member findMember = memberService.checkMember(memberId);
        Post post = checkPost(postId);
        checkPermission(findMember, post);

        deleteFile(post.getFileUrl());
        deleteFileUrl(post);
        log.info("게시글 파일 삭제 성공");
    }

    @Transactional
    void deleteFileUrl(Post post) {
        try {
            post.updateFileUrl("");
        } catch (Exception e) {
            log.error("deleteFileUrl() post에 파일 url update 실패 - message : {}", e.getMessage());
            throw new ApplicationException(ErrorCode.DATABASE_ERROR);
        }

    }

    void deleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty()) {  // file이 비어 있는데 삭제 버튼을 누를 수 있음을 방지
            try {
                fileHandler.delete(fileUrl);
            } catch (Exception e) {
                log.error("deleteFile() S3연계 post 파일 삭제 실패 - message : {}", e.getMessage());
                throw new ApplicationException(ErrorCode.FILE_ERROR);
            }

        }
    }


    @Transactional
    public void levelUpPostWriter(Long memberId, Long postId, MemberRole memberRole) {
        Member loginedMember = memberService.checkMember(memberId);
        if (!loginedMember.isAdmin()) throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);

        Post post = checkPost(postId);
        Member writer = memberService.checkMember(post.getMember().getId());
        writer.updateRole(memberRole);
        log.info("levelUpPostWriter() 성공");
    }

    public Post checkPost(Long postId) {
        return postReposiroty.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    // post 게시자 본인과 관리자만 할 수 있는 기능에 추가
    private void checkPermission(Member member, Post post) {
        if (member.getRole() != MemberRole.ADMIN && !member.getId().equals(post.getMember().getId())) {
            log.error("checkPermission() 실패 - member_role : {}, post_writer_id : {}, logined_member_id : {}", member.getRole(), post.getMember().getId(), member.getId());
            throw new ApplicationException(ErrorCode.ADMIN_OR_WRITER_PERMISSION);
        }
        log.info("checkPermission() 성공");
    }
    // role이 학생 이상인지
    private void checkMemberGradeHigherThanBasic(Member member) {
        if (!member.isStudent() && !member.isAdmin()) {
            throw new ApplicationException(ErrorCode.STUDENT_VALID_PERMISSION);
        }
    }
    private boolean hasPostingRestrictedByRole(Board board) {
        return !boardIdToName(board.getId()).equals("rating");
    }

    private String boardIdToName(Long boardId) {
        return boardService.boardIdToName(boardId);
    }

    private boolean hasFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    // ====== DTO, Entity 변환 =======
    // 다건 조회
    private PostListResponse postToPostListResponse(Post post) {
        return PostListResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .writer(post.getMember().getName())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .secret(post.getSecret())
                .build();
    }

    // 단건 조회
    private PostResponse postToPostResponse(Post post) {
        return PostResponse.builder()
                .title(post.getTitle())
                .writer(post.getMember().getName())
                .writerEmail(post.getMember().getEmail())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .fileUrl(!post.getFileUrl().isEmpty() ? fileHandler.getPresignedUrl(post.getFileUrl()) : "")
                .secret(post.getSecret())
                .commentAllow(post.getCommentAllow())
                .build();
    }



}