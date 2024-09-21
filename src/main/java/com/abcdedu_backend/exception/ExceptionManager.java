package com.abcdedu_backend.exception;

import com.abcdedu_backend.utils.Response;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Response<?>> applicationExceptionHandler(ApplicationException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(Response.error(ErrorResponse.of(e.getErrorCode().toString(), e.getMessage())));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Response<?>> databaseExceptionHandler(DataAccessException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.error(ErrorResponse.of(e.getCause().toString(), e.getMessage())));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<?>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        StringBuilder errorMessages = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMessages.append(fieldName).append(":").append(errorMessage).append(" ");
        });

        return ResponseEntity.status(ex.getStatusCode()).body(Response.error(ErrorResponse.of(ex.getStatusCode().toString(), errorMessages.toString().trim())));
    }
}
