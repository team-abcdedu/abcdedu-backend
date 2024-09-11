package com.abcdedu_backend.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Jwt 토큰을 이용한 인증을 위한 AuthenticationToken
 * 인증 전의 토큰을 전달하는 역할
 * UsernamePasswordAuthenticationToken를 참고하여 구현
 * @see org.springframework.security.authentication.UsernamePasswordAuthenticationToken
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String rawToken;


    public JwtAuthenticationToken(String rawToken) {
        super(null); // 인증 전
        this.setAuthenticated(false); // 인증 전
        this.rawToken = rawToken;
    }

    @Override
    public Object getCredentials() {
        return rawToken;
    }

    @Override
    public Object getPrincipal() {
        return rawToken;
    }
}

