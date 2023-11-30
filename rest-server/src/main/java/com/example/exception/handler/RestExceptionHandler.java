package com.example.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.ApiResponse;
import com.example.exception.RestException;

@RestControllerAdvice
@RestController
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
}
