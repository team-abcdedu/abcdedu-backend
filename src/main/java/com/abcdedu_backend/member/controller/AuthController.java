package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.member.controller.dto.LoginTokenDTO;
import com.abcdedu_backend.member.controller.dto.request.LoginRequest;
import com.abcdedu_backend.member.controller.dto.request.SignUpRequest;
import com.abcdedu_backend.member.controller.dto.response.LoginResponse;
import com.abcdedu_backend.member.controller.dto.response.ReissueResponse;
import com.abcdedu_backend.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증 기능", description = "로그인/회원가입 등 인증 관련 api입니다.")
public class AuthController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "회원가입을 합니다.")
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        memberService.signUp(signUpRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response, @Valid @RequestBody LoginRequest loginRequest){
        LoginTokenDTO loginTokenDto = memberService.login(loginRequest);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginTokenDto.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(14).toSeconds())
                .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());
        LoginResponse loginResponse = new LoginResponse(loginTokenDto.accessToken());
        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰으로 액세스 토큰을 재발급 합니다.")
    @GetMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(HttpServletRequest request) {
        Cookie refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
        String refreshToken = refreshTokenCookie.getValue();
        ReissueResponse reissueResponse = memberService.reissue(refreshToken);
        return ResponseEntity.ok(reissueResponse);
    }

}

