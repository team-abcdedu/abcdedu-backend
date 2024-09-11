package com.abcdedu_backend.global.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

/**
 * `RequestHeader`에서 `Authorization`을 확인하여 `JWT`를 추출하고 인증을 시도하는 필터
 * [BasicAuthenticationFilter]를 상속받아 구현
 * [JwtAuthenticationToken]을 생성하여 [AuthenticationManager]에게 인증을 요청한다.
 */
public class AuthorizationJwtHeaderFilter extends BasicAuthenticationFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public AuthorizationJwtHeaderFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if(header == null || !header.startsWith(BEARER_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        String rawToken = header.substring(BEARER_PREFIX.length());

        var authReq = new JwtAuthenticationToken(rawToken);
        Authentication authRes = super.getAuthenticationManager().authenticate(authReq);

        SecurityContextHolder.getContext().setAuthentication(authRes);

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // `/auth`로 시작하는 요청은 필터를 거치지 않는다.
        return request.getServletPath().startsWith("/auth");
    }
}
