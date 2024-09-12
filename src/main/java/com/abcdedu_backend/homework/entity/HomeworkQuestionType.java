package com.abcdedu_backend.homework.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HomeworkQuestionType {
    SUBJECTIVE(true,"주관식"),
    SINGLE_OPTION(true,"객관식 단일 선택"),
    MULTIPLE_OPTION(true,"객관식 다중 선택"),
    SHORT_ANSWER(true,"주관식 단답형"),

    FILL_IN_THE_BLANK(false,"빈칸 채우기"),
    SEQUENCE_MATCHING(false,"순서 맞추기"),
    FILE_UPLOAD(false,"파일 업로드");

    private final boolean active; // 활성화 여부
    private final String korean;
}
