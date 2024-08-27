package com.abcdedu_backend.exception;

import com.abcdedu_backend.utils.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Response<?>> AppExceptionHandler(ApplicationException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(Response.error(ErrorResponse.of(e.getErrorCode().toString(), e.getMessage())));
    }
}
