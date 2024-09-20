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
    THEORY("이론"),
    DATA("자료");




    private final String type;

    public static AssignmentType of (String type){
        return Stream.of(AssignmentType.values())
                .filter(a -> a.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_TYPE_NOT_FOUND));
    }
}
