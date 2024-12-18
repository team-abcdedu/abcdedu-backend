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
import com.abcdedu_backend.post.dto.request.PostCreateRequestV2;
import com.abcdedu_backend.post.dto.request.PostUpdateRequest;
import com.abcdedu_backend.post.dto.response.PostListResponse;
import com.abcdedu_backend.post.dto.request.PostCreateRequest;
import com.abcdedu_backend.post.dto.response.PostResponse;
import com.abcdedu_backend.post.entity.Post;
import com.abcdedu_backend.post.repository.PostReposiroty;
import jakarta.persistence.EntityNotFoundException;
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

    // 삭제 예정
    public Page<PostListResponse> getPosts(Long boardId, Pageable pageable) {
        Page<Post> findPostList = postReposiroty.findAllByBoardId(boardId, pageable);
        return findPostList.map(this::postToPostListResponse);
    }

    public Page<PostListResponse> getPostsV2(String boardName, Pageable pageable) {
        Page<Post> findPostList = postReposiroty.findAllByBoardName(boardName, pageable);
        return findPostList.map(this::postToPostListResponse);
    }

    @Transactional
    public PostResponse getPost(Long postId, Long memberId) {
        Post post = checkPost(postId);
        Member member = memberService.checkMember(memberId);
        checkMemberGradeHigherThanBasic(member);
        if (post.getSecret()) {
            checkPermission(member, post);
        }
        String imageUrl = !post.getFileUrl().isEmpty() ? fileHandler.getPresignedUrl(post.getFileUrl()) : "";

        // 이전 글 조회
        Post previousPost = postReposiroty.findFirstByIdLessThanAndBoardOrderByIdDesc(postId, post.getBoard())
                .orElse(null);

        // 다음 글 조회
        Post nextPost = postReposiroty.findFirstByIdGreaterThanAndBoardOrderByIdAsc(postId, post.getBoard())
                .orElse(null);

        post.increaseViewCount(); // 조회수 증가

        return PostResponse.of(post, previousPost, nextPost, imageUrl);
    }

    // Todo. 삭제 예정
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
    public Long createPost(PostCreateRequestV2 req, Long memberId, MultipartFile file) {
        Member member = memberService.checkMember(memberId);
        Board board = boardService.checkBoardThroughName(req.boardName());
        if (hasPostingRestrictedByRole(board)) checkMemberGradeHigherThanBasic(member); // 게시글은 학생 등급 이상만 생성할 수 있다, 예외 (등업게시판) : 모두 생성 가능
        if (hasPostingRestrictedByBoardType(board)) checkMemberGradeHigherThanAdmin(member); // 자료실 게시글 생성은 관리자만 가능하다.
        // 게시글 저장
        Post post = Post.of(member, board, req);
        post.changeBoard(board);
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
    @Transactional
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

        try {
            writer.updateRole(memberRole);
            log.info("1. member updateRole 성공");
            postReposiroty.delete(post);
            log.info("2. post 삭제 성공");
        } catch (Exception e) {
            log.error("등업 및 게시글 삭제 DB 실패, message : {}", e.getMessage());
            throw new ApplicationException(ErrorCode.DATABASE_ERROR);
        }
        log.info("멤버 등업 및 해당 등업 게시글 삭제 성공");
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
    // role이 관리자 이상인지
    private void checkMemberGradeHigherThanAdmin(Member member) {
        if (!member.isAdmin()) {
            throw new ApplicationException(ErrorCode.ADMIN_VALID_PERMISSION);
        }
    }
    private boolean hasPostingRestrictedByRole(Board board) {
        return !(board.getName().equals("rating"));
    }

    private boolean hasPostingRestrictedByBoardType(Board board) {
        return board.getName().equals("document");
    }

//    private String boardIdToName(Board board) {
//        return boardService.boardIdToName(board);
//    }

    private boolean hasFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    // ====== DTO, Entity 변환 =======
    // 다건 조회
    private PostListResponse postToPostListResponse(Post post) {
        String writer = "";
        String email = "";

        try {
            Member member = post.getMember();
            if (!member.isDeleted()) {
                writer = member.getName();
                email = member.getEmail();
            }
        } catch (EntityNotFoundException e){
            writer = "익명";
        }
        return PostListResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .writer(writer)
                .writerEmail(email)
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .secret(post.getSecret())
                .build();
    }



}