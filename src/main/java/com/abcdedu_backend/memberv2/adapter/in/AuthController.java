package com.abcdedu_backend.memberv2.adapter.in;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.memberv2.application.domain.LoginToken;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.LoginRequest;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.SignUpRequest;
import com.abcdedu_backend.memberv2.adapter.in.dto.response.LoginResponse;
import com.abcdedu_backend.memberv2.adapter.in.dto.response.ReissueResponse;
import com.abcdedu_backend.memberv2.application.AuthUseCase;
import com.abcdedu_backend.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료되었습니다."),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (RequestBody Validation)", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
})
@Tag(name = "인증 기능", description = "로그인/회원가입 등 인증 관련 api입니다.")
public class AuthController {

    @Value("${cookie.same-site}")
    private String cookieSameSite;
    @Value("${cookie.secure}")
    private boolean isCookieHttpSecure;

    private final AuthUseCase authUseCase;

    @Operation(summary = "회원 가입", description = "회원가입을 합니다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다.", content = @Content),
    })
    @PostMapping("/signup")
    public Response<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        authUseCase.signUp(signUpRequest.toCommand());
        return Response.success();
    }

    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "400", description = "존재하지 않는 이메일 또는 패스워드입니다.", content = @Content),
    })
    @PostMapping("/login")
    public Response<LoginResponse> login(HttpServletResponse response, @Valid @RequestBody LoginRequest loginRequest){
        LoginToken loginToken = authUseCase.login(loginRequest.email(), loginRequest.password());
        setRefreshTokenCookie(response, loginToken.refreshToken(), Duration.ofDays(14).toSeconds());
        LoginResponse loginResponse = new LoginResponse(loginToken.accessToken());
        return Response.success(loginResponse);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 합니다.")
    @DeleteMapping("/logout")
    public Response<Void> logout(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = parseRefreshToken(request);
        authUseCase.logout(refreshToken);
        setRefreshTokenCookie(response, "", 0L);
        return Response.success();
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, Long maxAge) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieSameSite)
                .secure(isCookieHttpSecure)
                .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());
    }



    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰으로 액세스 토큰을 재발급 합니다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.",content = @Content),
            @ApiResponse(responseCode = "401", description = "토큰이 존재하지 않습니다.",content = @Content),
    })
    @GetMapping("/reissue")
    public Response<ReissueResponse> reissue(HttpServletRequest request) {
        String refreshToken = parseRefreshToken(request);
        String reissueAccessToken = authUseCase.reissue(refreshToken);
        ReissueResponse reissueResponse = new ReissueResponse(reissueAccessToken);
        return Response.success(reissueResponse);
    }

    private String parseRefreshToken(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            Cookie refreshTokenCookie = Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_FOUND));
            String refreshToken = refreshTokenCookie.getValue();
            return refreshToken;
        } catch (Exception e){
            log.info(e.getLocalizedMessage());
            throw new ApplicationException(ErrorCode.TOKEN_NOT_FOUND);
        }
    }

}

