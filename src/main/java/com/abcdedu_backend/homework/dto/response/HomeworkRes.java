package com.abcdedu_backend.homework.dto.response;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HomeworkRes(
       Long id,
       String title,
       LocalDateTime updatedDate,
       String writer
) {
}
