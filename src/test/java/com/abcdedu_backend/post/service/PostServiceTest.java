package com.abcdedu_backend.post.service;


import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.service.MemberService;
import com.abcdedu_backend.post.dto.response.PostResponse;
import com.abcdedu_backend.post.entity.Post;
import com.abcdedu_backend.post.repository.PostReposiroty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService target;

    @Mock
    private PostReposiroty postReposiroty;

    @Mock
    private MemberService memberService;

    @Mock
    private FileHandler fileHandler;


    @Test
    void 게시글_조회() {
        Long postId = 1L;
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .email("test@gmail.com")
                .role(MemberRole.STUDENT)
                .build();

        Member writer = Member.builder()
                .id(2L)
                .email("ehdcjs159@gmail.com")
                .role(MemberRole.STUDENT)
                .build();

        Post prevPost = Post.builder()
                .id(postId - 1)
                .title("Previous Title")
                .build();

        Post nextPost = Post.builder()
                .id(postId + 1)
                .title("Next Title")
                .build();

        String presignedUrl = "https://example.com/sample-file-url";

        Post post = createPost(postId, writer);
        doReturn(Optional.of(post)).when(postReposiroty).findById(postId);
        doReturn(member).when(memberService).checkMember(memberId);
        doReturn(Optional.of(prevPost)).when(postReposiroty).findFirstByIdLessThanOrderByIdDesc(postId);
        doReturn(Optional.of(nextPost)).when(postReposiroty).findFirstByIdGreaterThanOrderByIdAsc(postId);
        doReturn(presignedUrl).when(fileHandler).getPresignedUrl("sample-file-url");

        PostResponse postResponse = target.getPost(postId, memberId);

        assertThat(postResponse).isNotNull();
        assertThat(postResponse.title()).isEqualTo(post.getTitle());
        assertThat(postResponse.writer()).isEqualTo(post.getMember().getName());
        assertThat(postResponse.writerEmail()).isEqualTo(post.getMember().getEmail());
        assertThat(postResponse.fileUrl()).isEqualTo(presignedUrl);
        assertThat(postResponse.prev().title()).isEqualTo(prevPost.getTitle());
        assertThat(postResponse.next().title()).isEqualTo(nextPost.getTitle());
    }

    @Test
    void 삭제된_유저_게시글_조회_가능() {
        Long postId = 1L;
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .email("test@gmail.com")
                .role(MemberRole.STUDENT)
                .build();

        Member writer = Member.builder()
                .id(2L)
                .email("ehdcjs159@gmail.com")
                .role(MemberRole.STUDENT)
                .deleted(true)
                .build();

        Post prevPost = Post.builder()
                .id(postId - 1)
                .title("Previous Title")
                .build();

        Post nextPost = Post.builder()
                .id(postId + 1)
                .title("Next Title")
                .build();

        String presignedUrl = "https://example.com/sample-file-url";

        Post post = createPost(postId, writer);
        doReturn(Optional.of(post)).when(postReposiroty).findById(postId);
        doReturn(member).when(memberService).checkMember(memberId);
        doReturn(Optional.of(prevPost)).when(postReposiroty).findFirstByIdLessThanOrderByIdDesc(postId);
        doReturn(Optional.of(nextPost)).when(postReposiroty).findFirstByIdGreaterThanOrderByIdAsc(postId);
        doReturn(presignedUrl).when(fileHandler).getPresignedUrl("sample-file-url");

        PostResponse postResponse = target.getPost(postId, memberId);

        assertThat(postResponse).isNotNull();
        assertThat(postResponse.title()).isEqualTo(post.getTitle());
        assertThat(postResponse.writer()).isEqualTo(post.getMember().getName());
        assertThat(postResponse.writerEmail()).isEqualTo(post.getMember().getEmail());
        assertThat(postResponse.fileUrl()).isEqualTo(presignedUrl);
        assertThat(postResponse.prev().title()).isEqualTo(prevPost.getTitle());
        assertThat(postResponse.next().title()).isEqualTo(nextPost.getTitle());
    }

    @Test
    void 이전_게시글_없으면_null_처리() {
        Long postId = 1L;
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .email("test@gmail.com")
                .role(MemberRole.STUDENT)
                .build();

        Member writer = Member.builder()
                .id(2L)
                .email("ehdcjs159@gmail.com")
                .role(MemberRole.STUDENT)
                .build();

        Post nextPost = Post.builder()
                .id(postId + 1)
                .title("Next Title")
                .build();

        String presignedUrl = "https://example.com/sample-file-url";

        Post post = createPost(postId, writer);
        doReturn(Optional.of(post)).when(postReposiroty).findById(postId);
        doReturn(member).when(memberService).checkMember(memberId);
        doReturn(Optional.empty()).when(postReposiroty).findFirstByIdLessThanOrderByIdDesc(postId);
        doReturn(Optional.of(nextPost)).when(postReposiroty).findFirstByIdGreaterThanOrderByIdAsc(postId);
        doReturn(presignedUrl).when(fileHandler).getPresignedUrl("sample-file-url");

        PostResponse postResponse = target.getPost(postId, memberId);

        assertThat(postResponse).isNotNull();
        assertThat(postResponse.title()).isEqualTo(post.getTitle());
        assertThat(postResponse.writer()).isEqualTo(post.getMember().getName());
        assertThat(postResponse.writerEmail()).isEqualTo(post.getMember().getEmail());
        assertThat(postResponse.fileUrl()).isEqualTo(presignedUrl);
        assertThat(postResponse.prev()).isNull();
        assertThat(postResponse.next()).isNotNull();
    }

    @Test
    void BASIC_권한_게시글_조회_실패() {
        Long postId = 1L;
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .email("test@gmail.com")
                .role(MemberRole.BASIC)
                .build();

        Member writer = Member.builder()
                .id(memberId)
                .email("ehdcjs159@gmail.com")
                .role(MemberRole.STUDENT)
                .build();

        Post post = createPost(postId, writer);
        doReturn(Optional.of(post)).when(postReposiroty).findById(postId);
        doReturn(member).when(memberService).checkMember(memberId);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> target.getPost(postId, memberId));

        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.STUDENT_VALID_PERMISSION);
    }




    private Post createPost(Long postId, Member member) {
        return Post.builder()
                .id(postId)
                .title("Title")
                .content("Content")
                .viewCount(10L)
                .commentCount(5L)
                .secret(false)
                .commentAllow(true)
                .fileUrl("sample-file-url")
                .member(member)
                .build();
    }
}
