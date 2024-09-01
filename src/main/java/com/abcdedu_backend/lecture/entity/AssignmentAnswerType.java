package com.abcdedu_backend.lecture.entity;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum AssignmentAnswerType {

    MULTIPLE_CHOICE("multipleChoice"),
    SUBJECTIVE("subjective"),
    OX("ox");

    private final String type;

    public static AssignmentAnswerType of (String type){
        return Stream.of(AssignmentAnswerType.values())
                .filter(a -> a.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_FOUND));
    }
}
