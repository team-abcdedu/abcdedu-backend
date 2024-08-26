package com.abcdedu_backend.interceptor;

import com.abcdedu_backend.member.entity.Member;
import com.abcdedu_backend.member.entity.MemberRole;
import com.abcdedu_backend.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//@Component
@Slf4j
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    public final static String LOGIN_MEMBER = "loginMem";
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(LOGIN_MEMBER);

        if (loginMember == null) {
            loginMember = memberRepository.findByName(LOGIN_MEMBER)
                            .orElseGet(()  -> {
                                Member newMember = Member.builder()
                                        .name(LOGIN_MEMBER)
                                        .email("loginMember@test.com")
                                        .role(MemberRole.BASIC)
                                        .encodedPassword("1234")
                                        .build();
                                return memberRepository.save(newMember);
                            });
            session.setAttribute(LOGIN_MEMBER, loginMember);
            log.info("임시 로그인 유저 설정 완료 : {}", ((Member)session.getAttribute(LOGIN_MEMBER)).getName());
        }

        return true;
    }
}
