package com.abcdedu_backend.lecture.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssignmentType {

    EXAM("시험"),
    THEORY("이론"),
    DATA("자료"),
    ANSWER("시험지");

    private final String type;
}
