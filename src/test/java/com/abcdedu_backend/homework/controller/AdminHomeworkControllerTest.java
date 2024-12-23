package com.abcdedu_backend.homework.controller;

import com.abcdedu_backend.exception.ExceptionManager;
import com.abcdedu_backend.homework.dto.request.RepresentativeRegisterRequest;
import com.abcdedu_backend.homework.service.HomeworkAdminService;
import com.abcdedu_backend.homework.service.HomeworkService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class AdminHomeworkControllerTest {

    @InjectMocks
    private AdminHomeworkController target;
    @Mock
    private HomeworkAdminService adminService;
    @Mock
    private HomeworkService homeworkService;

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
    void 대표과제_요청양식에_맞게_요청하고_성공하기() throws Exception {
        //given
        RepresentativeRegisterRequest request = new RepresentativeRegisterRequest(1L, 1L);
        // when & then
        mockMvc.perform(
                post("/admin/homeworks/representative")
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}