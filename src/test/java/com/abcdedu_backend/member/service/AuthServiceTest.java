package com.abcdedu_backend.member.service;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.global.jwt.JwtUtil;
import com.abcdedu_backend.member.domain.LoginToken;
import com.abcdedu_backend.member.adapter.in.dto.request.LoginRequest;
import com.abcdedu_backend.member.adapter.in.dto.request.SignUpRequest;
import com.abcdedu_backend.member.application.AuthService;
import com.abcdedu_backend.member.domain.Member;
import com.abcdedu_backend.member.domain.MemberRole;
import com.abcdedu_backend.member.domain.RefreshToken;
import com.abcdedu_backend.member.application.out.MemberRepository;
import com.abcdedu_backend.member.application.out.RefreshTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService target;
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
        SignUpRequest request = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "123456", "oo고등학교", 1234567L);
        Member savedMember = createMember();

        doReturn(Optional.empty()).when(memberRepository).findByEmail(request.email());
        doReturn("encodedPassword").when(passwordEncoder).encode("123456");
        doReturn(savedMember).when(memberRepository).save(any(Member.class));

        //when
        target.signUp(request.toCommand());

        //then
        verify(memberRepository, times(1)).findByEmail(request.email());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(passwordEncoder, times(1)).encode("123456");

    }

    @Test
    public void 이미있는_유저로_인해_회원가입_실패(){
        //given
        SignUpRequest request = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "123456", "oo고등학교", 1234567L);
        doReturn(Optional.of(createMember())).when(memberRepository).findByEmail(request.email());

        //when
        Assertions.assertThrows(ApplicationException.class, () -> target.signUp(request.toCommand()));

        //then
        verify(memberRepository).findByEmail(request.email());
    }

    @Test
    public void 로그인_성공(){
        //given
        LoginRequest request = new LoginRequest("ehdcjs159@gmail.com", "123456");
        Member member = createMember();
        RefreshToken refreshToken = new RefreshToken("refreshToken", member.getId());

        doReturn(true).when(passwordEncoder).matches("123456", "encodedPassword");
        doReturn(Optional.of(member)).when(memberRepository).findByEmail(request.email());
        doReturn(refreshToken).when(refreshTokenRepository).save(any(RefreshToken.class));
        doReturn("accessToken").when(jwtUtil).createAccessToken(any(Long.class));
        doReturn("refreshToken").when(jwtUtil).createRefreshToken(any(Long.class));

        //when
        LoginToken loginToken = target.login(request.email(), request.password());

        //then
        assertThat(loginToken.accessToken()).isEqualTo("accessToken");
        assertThat(loginToken.refreshToken()).isEqualTo("refreshToken");

        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
        verify(memberRepository, times(1)).findByEmail(request.email());
        verify(jwtUtil, times(1)).createAccessToken(any(Long.class));
        verify(jwtUtil, times(1)).createRefreshToken(any(Long.class));
        verify(passwordEncoder, times(1)).matches("123456", "encodedPassword");
    }

    @Test
    public void email_잘못_입력하여_로그인_실패(){
        //given
        LoginRequest request = new LoginRequest("asd123456@gmail.com", "123456");
        doReturn(Optional.empty()).when(memberRepository).findByEmail(request.email());

        //when
        Assertions.assertThrows(ApplicationException.class, () -> target.login(request.email(), request.password()));

        //then
        verify(memberRepository, times(1)).findByEmail(request.email());
    }

    @Test
    public void password_잘못_입력하여_로그인_실패(){
        //given
        LoginRequest request = new LoginRequest("asd123456@gmail.com", "123456789");
        Member member = createMember();

        doReturn(false).when(passwordEncoder).matches("123456789", "encodedPassword");
        doReturn(Optional.of(member)).when(memberRepository).findByEmail(request.email());

        //when
        Assertions.assertThrows(ApplicationException.class, () -> target.login(request.email(), request.password()));

        //then
        verify(memberRepository, times(1)).findByEmail(request.email());
        verify(passwordEncoder, times(1)).matches("123456789", "encodedPassword");
    }

    @Test
    public void AccessToken_재발급_성공() {
        //given
        String token = "refreshToken";
        RefreshToken refreshToken = createRefreshToken(token);

        doReturn(Optional.of(refreshToken)).when(refreshTokenRepository).findById(token);
        doReturn(1L).when(jwtUtil).getMemberIdFromRefreshToken(token);
        doReturn("accessToken").when(jwtUtil).createAccessToken(1L);

        //when
        target.reissue(token);

        //then
        verify(refreshTokenRepository, times(1)).findById(token);
        verify(jwtUtil, times(1)).getMemberIdFromRefreshToken(token);
        verify(jwtUtil, times(1)).createAccessToken(1L);
    }

    @Test
    public void 유효하지않은_refreshToken_AccessToken_재발급_실패() {
        //given
        String token = "refreshToken22";

        doReturn(Optional.empty()).when(refreshTokenRepository).findById(token);

        //when
        Assertions.assertThrows(ApplicationException.class, () -> target.reissue(token));

        //then
        verify(refreshTokenRepository, times(1)).findById(token);
    }

    @Test
    public void 로그아웃_성공() {
        //given
        String refreshToken = "refreshToken";

        //when
        target.logout(refreshToken);

        //then
        verify(refreshTokenRepository, times(1)).deleteById(refreshToken);
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
                .imageObjectKey("1")
                .build();
    }

    private RefreshToken createRefreshToken(String token){
        return RefreshToken.builder()
                .token(token)
                .id(1L)
                .build();
    }
}
