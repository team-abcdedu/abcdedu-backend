package com.abcdedu_backend.homework.dto.response;


import lombok.Builder;

import java.util.List;

@Builder
public record HomeworkReplyGetRes(
        List<String> questionHeaders,  // 각 질문의 열 제목
        List<List<String>> records    // 각 사람의 응답 레코드
) {
}
