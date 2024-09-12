package com.abcdedu_backend.infra.file;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import com.abcdedu_backend.lecture.entity.AssignmentAnswerType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum FileDirectory {
    PROFILE_IMAGE("프로필","abcdedu/profile/"),
    ASSIGNMENT_EXAM_FILE("시험","abcdedu/assignment/exam/"),
    ASSIGNMENT_THEORY_FILE("이론","abcdedu/assignment/theory/"),
    ASSIGNMENT_DATA_FILE("자료","abcdedu/assignment/data/"),
    POST_ATTACHMENT("게시글", "abcdedu/post/attachment/");

    private final String type;
    private final String directoryName;

    public static FileDirectory of (String type){
        return Stream.of(FileDirectory.values())
                .filter(a -> a.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.ASSIGNMENT_TYPE_NOT_FOUND));
    }
}
