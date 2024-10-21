package com.abcdedu_backend.lecture.controller;

import com.abcdedu_backend.exception.ExceptionManager;
import com.abcdedu_backend.global.security.SecurityConfig;
import com.abcdedu_backend.lecture.entity.AssignmentType;
import com.abcdedu_backend.lecture.service.LectureService;
import com.amazonaws.HttpMethod;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdminLectureControllerTest {

    @InjectMocks
    private AdminLectureController target;

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
    void 평가_파일_등록_성공() throws Exception {
        // Given
        final String url = "/admin/lectures/sub-lecture/1/file";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        // When
        final ResultActions resultActions = mockMvc.perform(
                multipart(url)
                        .file(file)
                        .param("assignmentType", "ANSWER")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );
        // Then
        resultActions.andExpect(status().isOk());
        verify(lectureService, times(1)).createAssignmentsFile(eq(1L), any(AssignmentType.class), any());
    }

    @Test
    void 파일이_없으면_등록_실패() throws Exception {
        // Given
        final String url = "/admin/lectures/sub-lecture/1/file";

        // When
        final ResultActions resultActions = mockMvc.perform(
                multipart(url)
                        .param("assignmentType", "ANSWER")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );
        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 파일이_빈값이면_등록_실패() throws Exception {
        // Given
        final String url = "/admin/lectures/sub-lecture/1/file";

        // When
        final ResultActions resultActions = mockMvc.perform(
                multipart(url)
                        .file(new MockMultipartFile("file", "", "multipart/form-data", "".getBytes()))
                        .param("assignmentType", "ANSWER")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );
        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void 평가_파일_수정_성공() throws Exception {
        // Given
        final String url = "/admin/lectures/file/1";
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        // When
        final ResultActions resultActions = mockMvc.perform(
                multipart(url)
                        .file(file)
                        .with(request -> {
                            request.setMethod(HttpMethod.PATCH.name());
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // Then
        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
        verify(lectureService, times(1)).updateAssignmentFile(eq(1L), any());
    }
}