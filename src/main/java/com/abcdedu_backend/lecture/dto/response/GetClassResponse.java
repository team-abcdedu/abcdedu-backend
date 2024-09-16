package com.abcdedu_backend.lecture.dto.response;

import com.abcdedu_backend.lecture.dto.SubClassDto;
import lombok.Builder;

import java.util.List;

@Builder
public record GetClassResponse(
        String title,
        String subTitle,
        String description,
        List<SubClassDto> subClasses
){
}
