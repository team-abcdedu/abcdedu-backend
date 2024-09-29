package com.abcdedu_backend.lecture.dto.response;

import com.abcdedu_backend.lecture.dto.SubClassDto;
import com.abcdedu_backend.lecture.entity.Lecture;
import com.abcdedu_backend.lecture.entity.SubLecture;
import lombok.Builder;

import java.util.List;

@Builder
public record GetLectureResponse(
        String title,
        String subTitle,
        String description,
        List<SubClassDto> subClasses
){

    public static GetLectureResponse createGetClassResponse(Lecture lecture) {
        return GetLectureResponse.builder()
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
