package com.abcdedu_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트가 없습니다."),
    SECRET_POST_INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "비밀글은 본인과 관리자만 열람이 가능합니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
    BOARD_DUPLICATION(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),

    //회원
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일 또는 비밀번호입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    //멤버
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "해당 권한이 없습니다."),

    //클래스
    CLASS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 클래스입니다."),
    SUB_CLASS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 서브클래스입니다."),
    ASSIGNMENT_ANSWER_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "지원하지 않는 응답 타입입니다."),
    ASSIGNMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 과제입니다.");


    private HttpStatus status;
    private String message;
}
