package com.abcdedu_backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "에러 응답")
public class ErrorResponse {
    @Schema(description = "에러 코드")
    private final String errorCode;
    @Schema(description = "에러 메세지")
    private final String message;

    public static ErrorResponse of(String errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }
}
