package com.abcdedu_backend.lecture.dto.response;

import com.abcdedu_backend.lecture.dto.SubClassDto;
import com.abcdedu_backend.lecture.entity.Lecture;
import com.abcdedu_backend.lecture.entity.SubLecture;
import lombok.Builder;

import java.util.List;

@Builder
public record GetClassResponse(
        String title,
        String subTitle,
        String description,
        List<SubClassDto> subClasses
){

    public static GetClassResponse createGetClassResponse(Lecture lecture) {
        return GetClassResponse.builder()
                .title(lecture.getTitle())
                .subTitle(lecture.getSubTitle())
                .description(lecture.getDescription())
                .subClasses(convertToSubClassesDto(lecture.getSubLectures()))
                .build();
    }

    private static List<SubClassDto> convertToSubClassesDto(List<SubLecture> subLectures) {
        return subLectures.stream()
                .map(SubClassDto::createSubClassDto)
                .toList();
    }
}
