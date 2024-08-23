package com.abcdedu_backend.member.service;

import com.abcdedu_backend.member.controller.dto.request.SignUpRequest;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService target;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void 회원가입_성공(){
        SignUpRequest request = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "123456");
        Member savedMember = createMember();
        doReturn(Optional.empty()).when(memberRepository).findByEmail(request.email());
        doReturn("encodedPassword").when(passwordEncoder).encode("123456");
        doReturn(savedMember).when(memberRepository).save(any(Member.class));

        target.signUp(request);

        verify(memberRepository, times(1)).findByEmail(request.email());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(passwordEncoder, times(1)).encode("123456");

    }

    @Test
    public void 이미있는_유저로_인해_회원가입_실패(){
        SignUpRequest request = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "123456");
        doReturn(Optional.of(createMember())).when(memberRepository).findByEmail(request.email());

        Assertions.assertThrows(IllegalStateException.class, () -> target.signUp(request));

        verify(memberRepository).findByEmail(request.email());
    }


    private Member createMember(){
        return Member.builder()
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .encodedPassword("encodedPassword")
                .build();
    }
}