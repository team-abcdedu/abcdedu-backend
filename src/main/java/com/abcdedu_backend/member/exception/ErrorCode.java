package com.abcdedu_backend.member.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    //회원 가입


    //로그인
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일 또는 비밀번호입니다.");

    private HttpStatus status;
    private String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
