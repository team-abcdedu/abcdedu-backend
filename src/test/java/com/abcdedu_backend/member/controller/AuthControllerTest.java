package com.abcdedu_backend.member.controller;


import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.exception.ExceptionManager;
import com.abcdedu_backend.member.dto.LoginTokenDTO;
import com.abcdedu_backend.member.dto.request.LoginRequest;
import com.abcdedu_backend.member.dto.request.SignUpRequest;
import com.abcdedu_backend.member.dto.response.ReissueResponse;
import com.abcdedu_backend.member.service.MemberService;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController target;
    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;
    private Gson gson;


    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new ExceptionManager())
                .build();
    }

    @Test
    void 회원가입_성공() throws Exception {
        //given
        final String url = "/auth/signup";
        SignUpRequest signUpRequest = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "1234567");
        doNothing().when(memberService).signUp(signUpRequest);

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 중복메일_회원가입_실패() throws Exception {
        //given
        final String url = "/auth/signup";
        SignUpRequest signUpRequest = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "1234567");
        doThrow(new ApplicationException(ErrorCode.EMAIL_ALREADY_EXISTS)).when(memberService).signUp(any(SignUpRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isConflict());
    }

    @Test
    void 패스워드_6자미만_회원가입_실패() throws Exception {
        //given
        final String url = "/auth/signup";
        SignUpRequest signUpRequest = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "12345");

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidSignUpRequests")
    void 회원가입_요청_유효성_검사_실패(SignUpRequest signUpRequest) throws Exception {
        //given
        final String url = "/auth/signup";

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("validSignUpRequests")
    void 회원가입_요청_유효성_검사_성공(SignUpRequest signUpRequest) throws Exception {
        //given
        final String url = "/auth/signup";

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk());
    }

    private static Stream<SignUpRequest> invalidSignUpRequests() {
        return Stream.of(
                new SignUpRequest("John Doe", "invalid-email", "validPassword"), // Invalid email
                new SignUpRequest("John Doe", "", "validPassword"), // Blank email

                new SignUpRequest("John Doe", "test@example.com", "12345"), // Password too short
                new SignUpRequest("John Doe", "test@example.com", "thisPasswordIsWayTooLongAndShouldFail"), // Password too long

                new SignUpRequest("", "test@example.com", "validPassword") // Blank name
        );
    }

    private static Stream<SignUpRequest> validSignUpRequests() {
        return Stream.of(
                new SignUpRequest("John Doe", "test@example.com", "validPassword"), // Valid case
                new SignUpRequest("Jane Doe", "jane@example.com", "anotherValidPass123") // Another valid case
        );
    }

    @Test
    void 어드민_회원가입_성공() throws Exception {
        //given
        final String url = "/auth/signup/admin";
        SignUpRequest signUpRequest = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "1234567");
        doNothing().when(memberService).adminSignUp(signUpRequest);

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 중복메일_어드민_회원가입_실패() throws Exception {
        //given
        final String url = "/auth/signup/admin";
        SignUpRequest signUpRequest = new SignUpRequest("고동천", "ehdcjs159@gmail.com", "1234567");
        doThrow(new ApplicationException(ErrorCode.EMAIL_ALREADY_EXISTS)).when(memberService).adminSignUp(any(SignUpRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isConflict());
    }

    @Test
    void 로그인_성공() throws Exception {
        //given
        final String url = "/auth/login";
        LoginRequest loginRequest = new LoginRequest("ehdcjs159@gmail.com", "1234567");
        LoginTokenDTO loginTokenDto = new LoginTokenDTO("accessTokenValue", "refreshTokenValue");

        when(memberService.login(loginRequest)).thenReturn(loginTokenDto);

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(cookie().value("refreshToken", "refreshTokenValue"));
    }

    @Test
    void 비밀번호_잘못입력으로_로그인_실패() throws Exception {
        //given
        final String url = "/auth/login";
        LoginRequest loginRequest = new LoginRequest("ehdcjs159@gmail.com", "wrongpassword");

        doThrow(new ApplicationException(ErrorCode.LOGIN_FAILED)).when(memberService).login(any(LoginRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 이메일_잘못입력으로_로그인_실패() throws Exception {
        //given
        final String url = "/auth/login";
        LoginRequest loginRequest = new LoginRequest("wrongemail@gmail.com", "1234567");

        doThrow(new ApplicationException(ErrorCode.LOGIN_FAILED)).when(memberService).login(any(LoginRequest.class));

        //when
        final ResultActions resultActions = mockMvc.perform(
                post(url)
                        .content(gson.toJson(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 로그아웃_성공() throws Exception {
        // given
        final String url = "/auth/logout";
        String refreshToken = "someRefreshToken";

        doNothing().when(memberService).logout(refreshToken);

        // when
        final ResultActions resultActions = mockMvc.perform(
                delete(url)
                        .cookie(new Cookie("refreshToken", refreshToken))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(cookie().value("refreshToken", ""))
                .andExpect(cookie().maxAge("refreshToken", 0));

    }

    @Test
    void 리이슈_성공() throws Exception {
        // given
        final String url = "/auth/reissue";
        String refreshToken = "validRefreshToken";
        ReissueResponse reissueResponse = new ReissueResponse("newAccessToken");

        when(memberService.reissue(refreshToken)).thenReturn(reissueResponse);

        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isOk());
    }

    @Test
    void 쿠키없음_재발급_실패() throws Exception {
        // given
        final String url = "/auth/reissue";

        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    void 잘못된토큰_재발급_실패() throws Exception {
        // given
        final String url = "/auth/reissue";
        String invalidRefreshToken = "invalidRefreshToken";

        doThrow(new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN)).when(memberService).reissue(invalidRefreshToken);

        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .cookie(new Cookie("refreshToken", invalidRefreshToken))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isUnauthorized());
    }



}