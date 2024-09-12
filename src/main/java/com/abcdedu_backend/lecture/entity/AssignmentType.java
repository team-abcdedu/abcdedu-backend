package com.abcdedu_backend.lecture.entity;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum AssignmentType {

    EXAM("시험"),
    PROJECT("프로젝트"),
    Lab("실습"),
    Theory("이론");


    private final String type;

    public static AssignmentType of (String type){
        return Stream.of(AssignmentType.values())
                .filter(a -> a.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_TYPE_NOT_FOUND));
    }
}
