package com.abcdedu_backend.member.service;

import com.abcdedu_backend.member.controller.dto.LoginTokenDTO;
import com.abcdedu_backend.member.controller.dto.request.LoginRequest;
import com.abcdedu_backend.member.controller.dto.request.SignUpRequest;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.exception.ErrorCode;
import com.abcdedu_backend.member.exception.UnauthorizedException;
import com.abcdedu_backend.member.repository.MemberRepository;
import com.abcdedu_backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signUp(SignUpRequest request){
        String signUpEmail = request.email();
        Optional<Member> findMember = memberRepository.findByEmail(signUpEmail);
        if (findMember.isPresent()){
            throw new IllegalStateException();
        }

        Member signUpMember = createMember(request);
        memberRepository.save(signUpMember);
    }

    public LoginTokenDTO login(LoginRequest request) {
        Member findMember = memberRepository.findByEmailAndEncodedPassword(request.email(), passwordEncoder.encode(request.password()))
                .orElseThrow(()-> new UnauthorizedException(ErrorCode.LOGIN_FAILED));

        String accessToken = jwtUtil.createAccessToken(findMember.getId());
        String refreshToken = jwtUtil.createRefreshToken(findMember.getId());

        return LoginTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Member createMember(SignUpRequest request) {
        Member signUpMember = Member.builder()
                .name(request.name())
                .email(request.email())
                .encodedPassword(passwordEncoder.encode(request.password()))
                .build();
        return signUpMember;
    }
}
