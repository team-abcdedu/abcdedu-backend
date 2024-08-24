package com.abcdedu_backend.member.service;

import com.abcdedu_backend.member.controller.dto.LoginTokenDTO;
import com.abcdedu_backend.member.controller.dto.request.LoginRequest;
import com.abcdedu_backend.member.controller.dto.request.SignUpRequest;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.RefreshToken;
import com.abcdedu_backend.member.exception.UnauthorizedException;
import com.abcdedu_backend.member.repository.MemberRepository;
import com.abcdedu_backend.member.repository.RefreshTokenRepository;
import com.abcdedu_backend.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private JwtUtil jwtUtil;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    public void 회원가입_성공(){
        //given
        SignUpRequest request = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "123456");
        Member savedMember = createMember();

        doReturn(Optional.empty()).when(memberRepository).findByEmail(request.email());
        doReturn("encodedPassword").when(passwordEncoder).encode("123456");
        doReturn(savedMember).when(memberRepository).save(any(Member.class));

        //when
        target.signUp(request);

        //then
        verify(memberRepository, times(1)).findByEmail(request.email());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(passwordEncoder, times(1)).encode("123456");

    }

    @Test
    public void 이미있는_유저로_인해_회원가입_실패(){
        //given
        SignUpRequest request = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "123456");
        doReturn(Optional.of(createMember())).when(memberRepository).findByEmail(request.email());

        //when
        Assertions.assertThrows(IllegalStateException.class, () -> target.signUp(request));

        //then
        verify(memberRepository).findByEmail(request.email());
    }

    @Test
    public void 로그인_성공(){
        //given
        LoginRequest request = new LoginRequest("ehdcjs159@gmail.com", "123456");
        Member member = createMember();
        RefreshToken refreshToken = new RefreshToken("refreshToken", member.getId());

        doReturn("encodedPassword").when(passwordEncoder).encode("123456");
        doReturn(true).when(passwordEncoder).matches("123456", "encodedPassword");
        doReturn(Optional.of(member)).when(memberRepository).findByEmail(request.email());
        doReturn(refreshToken).when(refreshTokenRepository).save(any(RefreshToken.class));
        doReturn("accessToken").when(jwtUtil).createAccessToken(any(Long.class));
        doReturn("refreshToken").when(jwtUtil).createRefreshToken(any(Long.class));

        //when
        LoginTokenDTO loginTokenDTO = target.login(request);

        //then
        assertThat(loginTokenDTO.accessToken()).isEqualTo("accessToken");
        assertThat(loginTokenDTO.refreshToken()).isEqualTo("refreshToken");

        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
        verify(memberRepository, times(1)).findByEmail(request.email());
        verify(jwtUtil, times(1)).createAccessToken(any(Long.class));
        verify(jwtUtil, times(1)).createRefreshToken(any(Long.class));
        verify(passwordEncoder, times(1)).matches("123456", "encodedPassword");
        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    public void email_잘못_입력하여_로그인_실패(){
        //given
        LoginRequest request = new LoginRequest("asd123456@gmail.com", "123456");
        doReturn("encodedPassword").when(passwordEncoder).encode("123456");
        doReturn(Optional.empty()).when(memberRepository).findByEmail(request.email());

        //when
        Assertions.assertThrows(UnauthorizedException.class, () -> target.login(request));

        //then
        verify(memberRepository, times(1)).findByEmail(request.email());
        verify(passwordEncoder, times(1)).encode("123456");
    }

    @Test
    public void password_잘못_입력하여_로그인_실패(){
        //given
        LoginRequest request = new LoginRequest("asd123456@gmail.com", "123456789");
        Member member = createMember();

        doReturn("encodedPassword222").when(passwordEncoder).encode("123456789");
        doReturn(false).when(passwordEncoder).matches("123456789", "encodedPassword");
        doReturn(Optional.of(member)).when(memberRepository).findByEmail(request.email());

        //when
        Assertions.assertThrows(UnauthorizedException.class, () -> target.login(request));

        //then
        verify(memberRepository, times(1)).findByEmail(request.email());
        verify(passwordEncoder, times(1)).matches("123456789", "encodedPassword");
        verify(passwordEncoder, times(1)).encode("123456789");
    }


    private Member createMember(){
        return Member.builder()
                .id(1L)
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword("encodedPassword")
                .build();
    }
}