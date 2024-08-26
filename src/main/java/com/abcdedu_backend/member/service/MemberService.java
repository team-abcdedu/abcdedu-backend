package com.abcdedu_backend.member.service;

import com.abcdedu_backend.member.controller.dto.LoginTokenDTO;
import com.abcdedu_backend.member.controller.dto.request.LoginRequest;
import com.abcdedu_backend.member.controller.dto.request.SignUpRequest;
import com.abcdedu_backend.member.controller.dto.response.MemberInfoResponse;
import com.abcdedu_backend.member.controller.dto.response.ReissueResponse;
import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.entity.RefreshToken;
import com.abcdedu_backend.member.exception.ErrorCode;
import com.abcdedu_backend.member.exception.UnauthorizedException;
import com.abcdedu_backend.member.repository.MemberRepository;
import com.abcdedu_backend.member.repository.RefreshTokenRepository;
import com.abcdedu_backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
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

    @Transactional
    public LoginTokenDTO login(LoginRequest request) {
        Member findMember = memberRepository.findByEmail(request.email())
                .orElseThrow(()-> new UnauthorizedException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password() ,findMember.getEncodedPassword())){
            throw new UnauthorizedException(ErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtUtil.createAccessToken(findMember.getId());
        String refreshToken = jwtUtil.createRefreshToken(findMember.getId());

        refreshTokenRepository.save(new RefreshToken(refreshToken, findMember.getId()));

        return LoginTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public ReissueResponse reissue(String refreshToken) {
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN));

        Long userId = jwtUtil.getMemberIdFromRefreshToken(refreshToken);

        String accessToken = jwtUtil.createAccessToken(userId);

        return new ReissueResponse(accessToken);
    }

    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.USER_NOT_FOUND));

        return MemberInfoResponse.builder()
                .studentId(findMember.getStudentId())
                .email(findMember.getEmail())
                .name(findMember.getName())
                .role(findMember.getRole().getName())
                .school(findMember.getSchool())
                .imageUrl(findMember.getImageUrl())
                .createdAt(findMember.getCreatedAt())
                .createPostCount(findMember.getPosts().size())
                .createCommentCount(findMember.getComments().size())
                .build();
    }

    private Member createMember(SignUpRequest request) {
        Member signUpMember = Member.builder()
                .name(request.name())
                .email(request.email())
                .encodedPassword(passwordEncoder.encode(request.password()))
                .role(MemberRole.BASIC)
                .build();
        return signUpMember;
    }
}
