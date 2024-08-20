package com.abcdedu_backend.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
public class HelloController {

    @RequestMapping("/")
    @ResponseBody
    public String hello() {
        log.info("{}", Thread.currentThread().getClass().getName());
        return "ok";
    }
}
