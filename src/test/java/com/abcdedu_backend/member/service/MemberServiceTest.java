package com.abcdedu_backend.member.service;

import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.member.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.member.dto.response.MemberBasicInfoResponse;
import com.abcdedu_backend.member.dto.response.MemberInfoResponse;
import com.abcdedu_backend.member.dto.response.MemberNameAndRoleResponse;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService target;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private FileHandler fileHandler;

    @Test
    public void 멤버프로필정보_조회_성공() {
        //given
        Member member = createMember();
        String expectedImageUrl = "imageUrl";
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());
        doReturn(expectedImageUrl).when(fileHandler).getPresignedUrl(member.getImageObjectKey());
        MemberInfoResponse expectedResponse = MemberInfoResponse.builder()
                .studentId(member.getStudentId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole().getName())
                .school(member.getSchool())
                .imageUrl(expectedImageUrl)
                .createdAt(member.getCreatedAt())
                .createPostCount(member.getPosts().size())
                .createCommentCount(member.getComments().size())
                .build();

        //when
        MemberInfoResponse memberInfoResponse = target.getMemberInfo(member.getId());

        //then
        verify(memberRepository, times(1)).findById(member.getId());
        verify(fileHandler, times(1)).getPresignedUrl(member.getImageObjectKey());
        assertThat(memberInfoResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void 멤버프로필정보_업데이트_성공() {
        // given
        Member member = createMember();
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());
        doReturn("updateObjectKey").when(fileHandler).upload(any(MultipartFile.class), eq(FileDirectory.PROFILE_IMAGE), eq(member.getId().toString()));

        // when
        target.updateMemberInfo(member.getId(), new UpdateMemberInfoRequest("테스트이름", "!!대학교", 31234L), mock(MultipartFile.class));

        // then
        verify(memberRepository, times(1)).findById(member.getId());
        verify(fileHandler, times(1)).upload(any(MultipartFile.class), eq(FileDirectory.PROFILE_IMAGE), eq(member.getId().toString()));
        assertThat(member.getName()).isEqualTo("테스트이름");
        assertThat(member.getSchool()).isEqualTo("!!대학교");
        assertThat(member.getStudentId()).isEqualTo(31234L);
        assertThat(member.getImageObjectKey()).isEqualTo("updateObjectKey");
    }

    @Test
    public void 멤버프로필정보_이름_역할_조회_성공() {
        Member member = createMember();
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());

        MemberNameAndRoleResponse memberNameAndRoleResponse = target.getMemberNameAndRoleInfo(member.getId());

        verify(memberRepository, times(1)).findById(member.getId());
        assertThat(memberNameAndRoleResponse.name()).isEqualTo(member.getName());
        assertThat(memberNameAndRoleResponse.role()).isEqualTo(member.getRole().getName());
    }

    @Test
    public void 멤버프로필_기본_정보_조회_성공() {
        Member member = createMember();
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());

        MemberBasicInfoResponse memberBasicInfoResponse = target.getMemberBasicInfo(member.getId());

        verify(memberRepository, times(1)).findById(member.getId());
        assertThat(memberBasicInfoResponse.name()).isEqualTo(member.getName());
        assertThat(memberBasicInfoResponse.role()).isEqualTo(member.getRole().getName());
        assertThat(memberBasicInfoResponse.email()).isEqualTo(member.getEmail());
    }

    @Test
    public void 비밀번호_변경_성공() {
        Member member = createMember();
        doReturn(Optional.of(member)).when(memberRepository).findByEmail(member.getEmail());
        doReturn("newEncodedPassword").when(passwordEncoder).encode("123456");

        target.updatePassword(member.getEmail(), "123456");

        verify(memberRepository, times(1)).findByEmail(member.getEmail());
        verify(passwordEncoder, times(1)).encode("123456");
        assertThat(member.getEncodedPassword()).isEqualTo("newEncodedPassword");
    }


    private Member createMember(){
        return Member.builder()
                .id(1L)
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword("encodedPassword")
                .imageObjectKey("1")
                .role(MemberRole.BASIC)
                .school("~~대학교")
                .studentId(1234567L)
                .posts(new ArrayList<>())
                .comments(new ArrayList<>())
                .imageObjectKey("1")
                .build();
    }

}