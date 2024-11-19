package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.exception.ExceptionManager;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.memberv2.adapter.in.dto.request.UpdatePasswordRequest;
import com.abcdedu_backend.memberv2.adapter.in.MemberInfoController;
import com.abcdedu_backend.memberv2.application.MemberInfoUseCase;
import com.abcdedu_backend.memberv2.application.dto.MemberBasicInfoDto;
import com.abcdedu_backend.memberv2.application.dto.MemberInfoDto;
import com.abcdedu_backend.memberv2.application.dto.NameAndRoleDto;
import com.abcdedu_backend.memberv2.application.dto.command.UpdateMemberInfoCommand;
import com.amazonaws.HttpMethod;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MemberInfoControllerTest {

    @InjectMocks
    private MemberInfoController target;
    @Mock
    private MemberInfoUseCase memberInfoUseCase;

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
    void 프로필_조회_성공() throws Exception {
        // given
        final String url = "/members/info";
        Long memberId = 1L;
        LocalDateTime now = LocalDateTime.now();
        MemberInfoDto memberInfoDto = MemberInfoDto.builder()
                .name("고동천")
                .email("ehdcjs159@gmail.com")
                .school("~~대학교")
                .studentId(202312345L)
                .imageUrl("https://example.com/profile.jpg")
                .role("새싹")
                .createdAt(now)
                .createPostCount(10)
                .createCommentCount(5)
                .build();

        doReturn(memberInfoDto).when(memberInfoUseCase).getMemberInfo(memberId);

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", memberId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value("고동천"))
                .andExpect(jsonPath("$.result.role").value("새싹"))
                .andExpect(jsonPath("$.result.email").value("ehdcjs159@gmail.com"))
                .andExpect(jsonPath("$.result.school").value("~~대학교"))
                .andExpect(jsonPath("$.result.studentId").value(202312345L))
                .andExpect(jsonPath("$.result.imageUrl").value("https://example.com/profile.jpg"))
                .andExpect(jsonPath("$.result.createPostCount").value(10))
                .andExpect(jsonPath("$.result.createCommentCount").value(5));
    }

    @Test
    void 프로필_조회_실패_없는멤버() throws Exception {
        // given
        final String url = "/members/info";
        Long memberId = 2L;

        doThrow(new ApplicationException(ErrorCode.USER_NOT_FOUND)).when(memberInfoUseCase).getMemberInfo(memberId);

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", memberId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 프로필_정보_수정_성공() throws Exception {
        // given
        final String url = "/members/info";
        Long memberId = 1L;
        UpdateMemberInfoRequest updateRequest = new UpdateMemberInfoRequest("고동천", "!!대학교", 202356789L);
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "file content".getBytes());

        UpdateMemberInfoCommand updateMemberInfoCommand = UpdateMemberInfoCommand.of(
                memberId,
                updateRequest.name(),
                updateRequest.school(),
                updateRequest.studentId(),
                file);

        doNothing().when(memberInfoUseCase).updateMemberInfo(updateMemberInfoCommand);

        // when
        final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                        .file(file)
                        .param("name", updateRequest.name())
                        .param("school", updateRequest.school())
                        .param("studentId", updateRequest.studentId().toString())
                        .param("memberId", memberId.toString())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(request -> {
                            request.setMethod(HttpMethod.PATCH.name());
                            return request;
                        })
                );

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void 프로필_이름_역할_정보_조회_성공() throws Exception {
        // given
        final String url = "/members/info/name-and-role";
        Long memberId = 1L;
        NameAndRoleDto nameAndRoleDto = new NameAndRoleDto("고동천", "관리자");
        doReturn(nameAndRoleDto).when(memberInfoUseCase).getMemberNameAndRoleInfo(memberId);

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", memberId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value(nameAndRoleDto.name()))
                .andExpect(jsonPath("$.result.role").value(nameAndRoleDto.role()));
    }

    @Test
    void 프로필_이름_역할_정보_조회_실패_없는멤버() throws Exception {
        // given
        final String url = "/members/info/name-and-role";
        Long memberId = 2L;
        doThrow(new ApplicationException(ErrorCode.USER_NOT_FOUND)).when(memberInfoUseCase).getMemberNameAndRoleInfo(memberId);

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", memberId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 프로필_기본_정보_조회_성공() throws Exception {
        // given
        final String url = "/members/basic-info";
        Long memberId = 1L;
        MemberBasicInfoDto memberBasicInfoDto = new MemberBasicInfoDto("고동천", "관리자", "ehdcjs159@gmail.com");
        doReturn(memberBasicInfoDto).when(memberInfoUseCase).getMemberBasicInfo(memberId);

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", memberId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value(memberBasicInfoDto.name()))
                .andExpect(jsonPath("$.result.role").value(memberBasicInfoDto.role()))
                .andExpect(jsonPath("$.result.email").value(memberBasicInfoDto.email()));
    }

    @Test
    void 프로필_기본_정보_조회_실패_없는멤버() throws Exception {
        // given
        final String url = "/members/basic-info";
        Long memberId = 2L;
        doThrow(new ApplicationException(ErrorCode.USER_NOT_FOUND)).when(memberInfoUseCase).getMemberBasicInfo(memberId);

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", memberId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 비밀번호_변경_성공() throws Exception {
        // given
        final String url = "/members/password";

        UpdatePasswordRequest request = new UpdatePasswordRequest("123456");
        Long memberId = 1L;

        doNothing().when(memberInfoUseCase).updatePassword(memberId, request.newPassword());

        // when
        final ResultActions resultActions = mockMvc.perform(
                patch(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", memberId.toString())
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 없는_유저로_비밀번호_변경_실패() throws Exception {
        // given
        final String url = "/members/password";

        UpdatePasswordRequest request = new UpdatePasswordRequest("123456");
        Long notFoundMemberId = 2L;
        doThrow(new ApplicationException(ErrorCode.USER_NOT_FOUND)).when(memberInfoUseCase).updatePassword(notFoundMemberId, request.newPassword());

        // when
        final ResultActions resultActions = mockMvc.perform(
                patch(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", notFoundMemberId.toString())
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNotFound());
    }
}