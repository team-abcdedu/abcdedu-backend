package com.abcdedu_backend.member.application;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.global.jwt.JwtUtil;
import com.abcdedu_backend.member.application.dto.command.SignupCommand;
import com.abcdedu_backend.member.domain.LoginToken;
import com.abcdedu_backend.member.domain.Member;
import com.abcdedu_backend.member.domain.MemberRole;
import com.abcdedu_backend.member.domain.RefreshToken;
import com.abcdedu_backend.member.application.out.MemberRepository;
import com.abcdedu_backend.member.application.out.RefreshTokenRepository;
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
public class AuthService implements AuthUseCase{

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Override
    @Transactional
    public void signUp(SignupCommand command){
        checkDuplicateEmail(command.email());
        Member signUpMember = createBasicMember(command);
        memberRepository.save(signUpMember);
    }

    @Override
    @Transactional
    public LoginToken login(String email, String password) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(()-> new ApplicationException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(password ,findMember.getEncodedPassword())){
            throw new ApplicationException(ErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtUtil.createAccessToken(findMember.getId());
        String refreshToken = jwtUtil.createRefreshToken(findMember.getId());

        refreshTokenRepository.save(new RefreshToken(refreshToken, findMember.getId()));

        return LoginToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String reissue(String refreshToken) {
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN));

        Long memberId = jwtUtil.getMemberIdFromRefreshToken(refreshToken);

        String accessToken = jwtUtil.createAccessToken(memberId);

        return accessToken;
    }

    @Transactional
    @Override
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    public void checkDuplicateEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new ApplicationException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private Member createBasicMember(SignupCommand command) {
        return createMember(command, MemberRole.BASIC);
    }

    private Member createMember(SignupCommand command, MemberRole role) {
        Member member = Member.builder()
                .name(command.name())
                .email(command.email())
                .encodedPassword(passwordEncoder.encode(command.password()))
                .role(role)
                .school(command.school())
                .studentId(command.studentId())
                .build();
        return member;
    }
}
