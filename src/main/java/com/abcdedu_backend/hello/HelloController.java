package com.abcdedu_backend.hello;

import com.abcdedu_backend.interceptor.LoginInterceptor;
import com.abcdedu_backend.interceptor.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes(LoginInterceptor.LOGIN_MEMBER)
@Controller
@Slf4j
public class HelloController {

    @GetMapping("/")
    @ResponseBody
    public String hello(@SessionAttribute(LoginInterceptor.LOGIN_MEMBER)Member loginMember) {
        log.info("{}님 안녕하세요", loginMember.getName());
        return "ok";
    }


}
