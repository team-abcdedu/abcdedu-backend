package com.abcdedu_backend.lecture.dto;

import lombok.Builder;

@Builder
public record SubClassDto (
        String title,
        Integer orderNumber,
        String description,
        Long subClassId
){

}
