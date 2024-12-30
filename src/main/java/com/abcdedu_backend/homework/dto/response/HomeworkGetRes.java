package com.abcdedu_backend.homework.dto.response;


import lombok.Builder;

import java.util.List;

@Builder
public record HomeworkGetRes(
        Long homeworkId, // 대표 과제 id
        String title, // 과제 제목
        String description, // 과제에 대한 부연 설명

        String additionalDescription, // 과제에 대한 부연 설명2
        List<HomeworkQuestionGetRes> questionGetResponses
) {
}
