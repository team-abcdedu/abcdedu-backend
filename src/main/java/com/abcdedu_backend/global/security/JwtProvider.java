package com.abcdedu_backend.global.security;


import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.global.jwt.JwtUtil;
import com.abcdedu_backend.member.application.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtProvider implements AuthenticationProvider {
    private final JwtUtil jwtUtil;
    private static final String ROLE_PREFIX = "ROLE_";

    /**
     * JwtAuthenticationToken을 받아서 인증을 진행
     * 토큰이 유효하지 않은 경우 예외를 발생시킴
     * 유효한경우 SecurityContextHolder에 인증정보를 저장
     * 그후 UsernamePasswordAuthenticationToken을 반환
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        Long memberId;
        try {
            memberId = jwtUtil.getMemberIdFromAccessToken(token);
        }catch (ApplicationException e){
            // 시큐리티 인증 예외로 변환
            throw new AuthenticationServiceException(ErrorCode.INVALID_ACCESS_TOKEN.getMessage());
        }
        // JWT에 Role 정보가 없으므로 기본 토큰 검증에서는 BASIC 권한을 부여
        LoginUserDetails loginUserDetails = new LoginUserDetails(memberId, MemberRole.BASIC);

        // 검증 후 인증정보 Authentication 객체를 반환
        return createAuthenticationToken(loginUserDetails);
    }



    private UsernamePasswordAuthenticationToken createAuthenticationToken(LoginUserDetails loginUserDetails) {
        Set<SimpleGrantedAuthority> authorities
            = Set.of(new SimpleGrantedAuthority(ROLE_PREFIX + MemberRole.BASIC.name()));
        return new UsernamePasswordAuthenticationToken(loginUserDetails, null, authorities);
    }

    /**
     * JwtAuthenticationToken 타입의 토큰을 지원하는 경우에만 인증을 진행
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
