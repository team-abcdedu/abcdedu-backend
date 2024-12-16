package com.abcdedu_backend.homework.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HomeworkReplyReadReq(
        Long homeworkId,
        LocalDateTime fromDate,
        LocalDateTime toDate

) {

}
