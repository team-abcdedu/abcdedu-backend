package com.abcdedu_backend.member.controller;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.exception.ExceptionManager;
import com.abcdedu_backend.member.dto.request.UpdateMemberInfoRequest;
import com.abcdedu_backend.member.dto.response.MemberInfoResponse;
import com.abcdedu_backend.member.dto.response.MemberNameAndRoleResponse;
import com.abcdedu_backend.member.service.MemberService;
import com.amazonaws.HttpMethod;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MemberInfoControllerTest {

    @InjectMocks
    private MemberInfoController target;
    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;


    @BeforeEach
    public void init() {
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
        MemberInfoResponse memberInfoResponse = MemberInfoResponse.builder()
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

        doReturn(memberInfoResponse).when(memberService).getMemberInfo(memberId);

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

        doThrow(new ApplicationException(ErrorCode.USER_NOT_FOUND)).when(memberService).getMemberInfo(memberId);

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

        doNothing().when(memberService).updateMemberInfo(
                any(Long.class),
                any(UpdateMemberInfoRequest.class),
                any(MultipartFile.class)
        );

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
        MemberNameAndRoleResponse nameAndRoleResponse = new MemberNameAndRoleResponse("고동천", "관리자");
        doReturn(nameAndRoleResponse).when(memberService).getMemberNameAndRoleInfo(memberId);

        // when
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header("Authorization", "Bearer validToken")
                        .param("memberId", memberId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.name").value(nameAndRoleResponse.name()))
                .andExpect(jsonPath("$.result.role").value(nameAndRoleResponse.role()));
    }

    @Test
    void 프로필_이름_역할_정보_조회_실패_없는멤버() throws Exception {
        // given
        final String url = "/members/info/name-and-role";
        Long memberId = 2L;
        doThrow(new ApplicationException(ErrorCode.USER_NOT_FOUND)).when(memberService).getMemberNameAndRoleInfo(memberId);

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
}