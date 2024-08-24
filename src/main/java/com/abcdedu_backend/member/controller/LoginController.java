package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.member.controller.dto.LoginTokenDTO;
import com.abcdedu_backend.member.controller.dto.request.LoginRequest;
import com.abcdedu_backend.member.controller.dto.request.SignUpRequest;
import com.abcdedu_backend.member.controller.dto.response.LoginResponse;
import com.abcdedu_backend.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        memberService.signUp(signUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response, @Valid @RequestBody LoginRequest loginRequest){
        LoginTokenDTO loginTokenDto = memberService.login(loginRequest);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginTokenDto.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(14).toMillis())
                .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());
        LoginResponse loginResponse = new LoginResponse(loginTokenDto.accessToken());
        return ResponseEntity.ok(loginResponse);
    }

}

