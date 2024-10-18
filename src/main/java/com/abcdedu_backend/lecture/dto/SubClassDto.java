package com.abcdedu_backend.lecture.dto;

import com.abcdedu_backend.lecture.entity.SubLecture;
import lombok.Builder;

@Builder
public record SubClassDto (
        String title,
        Integer orderNumber,
        String description,
        Long subClassId
){
    public static SubClassDto of(SubLecture subLecture) {
        return SubClassDto.builder()
                .title(subLecture.getTitle())
                .orderNumber(subLecture.getOrderNumber())
                .description(subLecture.getDescription())
                .subClassId(subLecture.getId())
                .build();
    }
}
