package com.abcdedu_backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    public final static String LOGIN_MEMBER = "loginMember";
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            loginMember = memberRepository.findByName(LOGIN_MEMBER)
                            .orElseGet(()  -> {
                                Member newMember = new Member(LOGIN_MEMBER, "loginMember@test.com", MemberRole.BASIC);
                                return memberRepository.save(newMember);
                            });
            session.setAttribute(LOGIN_MEMBER, loginMember);
            log.info("임시 로그인 유저 설정 완료 : {}", ((Member)session.getAttribute(LOGIN_MEMBER)).getName());
        }

        return true;
    }
}
