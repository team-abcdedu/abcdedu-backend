package com.abcdedu_backend.homework.dto.response;


import com.abcdedu_backend.homework.entity.Homework;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HomeworkRes(
       Long id,
       String title,
       LocalDateTime updatedDate,
       String writer
) {

    public static HomeworkRes fromHomework(Homework h) {
        return new HomeworkRes(
                h.getId(),
                h.getTitle(),
                h.getUpdatedAt(),
                h.getMember().getName());
    }
}
