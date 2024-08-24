package com.abcdedu_backend.member.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{
    private final String message;
    public UnauthorizedException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.message = errorCode.getMessage();
    }
}
