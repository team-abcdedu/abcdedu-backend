package com.abcdedu_backend.member.service;

import com.abcdedu_backend.infra.file.FileDirectory;
import com.abcdedu_backend.infra.file.FileHandler;
import com.abcdedu_backend.member.application.MemberInfoService;
import com.abcdedu_backend.member.domain.Member;
import com.abcdedu_backend.member.domain.MemberRole;
import com.abcdedu_backend.member.application.dto.MemberBasicInfoDto;
import com.abcdedu_backend.member.application.dto.MemberInfoDto;
import com.abcdedu_backend.member.application.dto.NameAndRoleDto;
import com.abcdedu_backend.member.application.dto.command.UpdateMemberInfoCommand;
import com.abcdedu_backend.member.application.out.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberInfoServiceTest {

    @InjectMocks
    private MemberInfoService target;
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
        MemberInfoDto expectedDto = MemberInfoDto.builder()
                .id(member.getId())
                .studentId(member.getStudentId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole().getName())
                .school(member.getSchool())
                .imageUrl(expectedImageUrl)
                .createdAt(member.getCreatedAt())
                .createPostCount(member.getPostCount())
                .createCommentCount(member.getCommentCount())
                .build();

        //when
        MemberInfoDto memberInfoDto = target.getMemberInfo(member.getId());

        //then
        verify(memberRepository, times(1)).findById(member.getId());
        verify(fileHandler, times(1)).getPresignedUrl(member.getImageObjectKey());
        assertThat(memberInfoDto).isEqualTo(expectedDto);
    }

    @Test
    public void 멤버프로필정보_업데이트_성공() {
        // given
        Member member = createMember();
        Member updatedMember = Member.builder()
                .id(member.getId())
                .name("테스트이름")
                .school("!!대학교")
                .studentId(31234L)
                .imageObjectKey("updateObjectKey")
                .build();

        doReturn(updatedMember).when(memberRepository).updateMemberInfo(updatedMember.getId(), updatedMember.getName(), updatedMember.getSchool(), updatedMember.getStudentId(), updatedMember.getImageObjectKey());
        doReturn("updateObjectKey").when(fileHandler).upload(any(MultipartFile.class), eq(FileDirectory.PROFILE_IMAGE), eq(member.getId().toString()));

        UpdateMemberInfoCommand updateMemberInfoCommand = UpdateMemberInfoCommand.of(
                member.getId(),
                "테스트이름",
                "!!대학교",
                31234L,
                mock(MultipartFile.class));
        // when
        target.updateMemberInfo(updateMemberInfoCommand);

        // then
        verify(fileHandler, times(1)).upload(any(MultipartFile.class), eq(FileDirectory.PROFILE_IMAGE), eq(member.getId().toString()));
        assertThat(updatedMember.getName()).isEqualTo("테스트이름");
        assertThat(updatedMember.getSchool()).isEqualTo("!!대학교");
        assertThat(updatedMember.getStudentId()).isEqualTo(31234L);
        assertThat(updatedMember.getImageObjectKey()).isEqualTo("updateObjectKey");
    }

    @Test
    public void 멤버프로필정보_이름_역할_조회_성공() {
        Member member = createMember();
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());

        NameAndRoleDto nameAndRoleDto = target.getMemberNameAndRoleInfo(member.getId());

        verify(memberRepository, times(1)).findById(member.getId());
        assertThat(nameAndRoleDto.name()).isEqualTo(member.getName());
        assertThat(nameAndRoleDto.role()).isEqualTo(member.getRole().getName());
    }

    @Test
    public void 멤버프로필_기본_정보_조회_성공() {
        Member member = createMember();
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());

        MemberBasicInfoDto memberBasicInfoDto = target.getMemberBasicInfo(member.getId());

        verify(memberRepository, times(1)).findById(member.getId());
        assertThat(memberBasicInfoDto.name()).isEqualTo(member.getName());
        assertThat(memberBasicInfoDto.role()).isEqualTo(member.getRole().getName());
        assertThat(memberBasicInfoDto.email()).isEqualTo(member.getEmail());
    }

    @Test
    public void 비밀번호_변경_성공() {
        Member member = createMember();
        String newPassword = "newEncodedPassword";
        Member updateMember = updatedPasswordMember(newPassword);
        doReturn(newPassword).when(passwordEncoder).encode("123456");
        doReturn(updateMember).when(memberRepository).updatePassword(member.getEmail(), newPassword);

        target.updatePassword(member.getEmail(), "123456");

        verify(memberRepository, times(1)).updatePassword(member.getEmail(), newPassword);
        verify(passwordEncoder, times(1)).encode("123456");
        assertThat(updateMember.getEncodedPassword()).isEqualTo(newPassword);
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
                .postCount(1)
                .commentCount(1)
                .imageObjectKey("1")
                .build();
    }

    private Member updatedPasswordMember(String newPassword){
        return Member.builder()
                .id(1L)
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword(newPassword)
                .imageObjectKey("1")
                .role(MemberRole.BASIC)
                .school("~~대학교")
                .studentId(1234567L)
                .postCount(1)
                .commentCount(1)
                .imageObjectKey("1")
                .build();
    }

}