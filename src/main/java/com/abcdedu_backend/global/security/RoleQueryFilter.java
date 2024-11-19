package com.abcdedu_backend.global.security;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.member.application.domain.Member;
import com.abcdedu_backend.member.application.out.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RoleQueryFilter extends OncePerRequestFilter {
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 인증정보를 얻지 못한 경우(방어로직) : errorHandler에서 처리되도록 doFilter를 호출
        var context = SecurityContextHolder.getContext();
        if(context.getAuthentication() == null){
            filterChain.doFilter(request, response);
            return;
        }

        LoginUserDetails loginUserDetails = (LoginUserDetails) context.getAuthentication().getPrincipal();

        Member member = memberRepository.findById(loginUserDetails.getLoginUserId())
            .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        LoginUserDetails newLoginUserDetails = new LoginUserDetails(member.getId(), member.getRole());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(newLoginUserDetails, null, newLoginUserDetails.getAuthorities()));

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().startsWith("/admin");
    }
}
