package com.example.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.exception.ApiResponse;
import com.example.exception.RestException;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(
        RestException.class
    )
    public ResponseEntity<ApiResponse> errorHandle(RestException ex){
        ApiResponse response = ApiResponse.builder()
                                    .status(String.valueOf(ex.getStatus().value()))
                                    .code(ex.getCode())
                                    .message(ex.getMessage())
                                    .build();
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ApiResponse> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        
        ApiResponse response = ApiResponse.builder()
                .status(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .code("99999")
                .message(ex.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
