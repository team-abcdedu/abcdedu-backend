package com.abcdedu_backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    public final static String LOGIN_MEMBER = "loginMember";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            session.setAttribute(LOGIN_MEMBER, new Member(LOGIN_MEMBER, "loginMember@naver.com", MemberRole.BASIC));
            log.info("임시 로그인 유저 설정 완료 : {}", ((Member)session.getAttribute(LOGIN_MEMBER)).getName());
        }

        return true;
    }
}
