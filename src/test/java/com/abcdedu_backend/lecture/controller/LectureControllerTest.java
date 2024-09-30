package com.abcdedu_backend.lecture.controller;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.exception.ExceptionManager;
import com.abcdedu_backend.lecture.dto.response.GetAssignmentFileUrlResponse;
import com.abcdedu_backend.lecture.dto.response.GetAssignmentResponseV1;
import com.abcdedu_backend.lecture.dto.response.GetClassResponse;
import com.abcdedu_backend.lecture.service.LectureService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LectureControllerTest {

    @InjectMocks
    private LectureController target;

    @Mock
    private LectureService lectureService;

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
    void 클래스_조회_성공() throws Exception {
        // Given
        final String url = "/lectures";
        List<GetClassResponse> classResponses = List.of(new GetClassResponse("title", "subtitle", "description", List.of()));

        doReturn(classResponses).when(lectureService).getLectures();

        // When
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk());
        verify(lectureService, times(1)).getLectures();
    }

    @Test
    void 평가_파일_리스트_조회_성공() throws Exception {
        // Given
        final String url = "/lectures/sub-lecture/1";
        List<GetAssignmentResponseV1> assignments = List.of(new GetAssignmentResponseV1("시험",1L));

        doReturn(assignments).when(lectureService).getAssignments(1L);

        // When
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk());
        verify(lectureService, times(1)).getAssignments(1L);
    }

    @Test
    void 평가_파일_URL_조회_성공() throws Exception {
        // Given
        final String url = "/lectures/file/1";
        GetAssignmentFileUrlResponse fileUrlResponse = new GetAssignmentFileUrlResponse("http://example.com/presigned-url");

        doReturn(fileUrlResponse).when(lectureService).getAssignmentFileUrl(1L, 1L);

        // When
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .param("memberId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk());
        verify(lectureService, times(1)).getAssignmentFileUrl(1L, 1L);
    }

    @Test
    void 평가_파일_URL_조회_권한_없음_실패() throws Exception {
        // Given
        final String url = "/lectures/file/1";

        doThrow(new ApplicationException(ErrorCode.STUDENT_VALID_PERMISSION))
                .when(lectureService)
                .getAssignmentFileUrl(1L, 1L);

        // When
        final ResultActions resultActions = mockMvc.perform(
                get(url)
                        .param("memberId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isForbidden());
        verify(lectureService, times(1)).getAssignmentFileUrl(1L, 1L);
    }
}