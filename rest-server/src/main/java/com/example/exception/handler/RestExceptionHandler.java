package com.example.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import com.example.exception.RestException;
import com.example.vo.ApiResponse;

import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class RestExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);
    @ExceptionHandler(
        RestException.class
    )
    public ResponseEntity<ApiResponse> errorHandle(RestException e){
        LOGGER.error("error", e);
        ApiResponse response = ApiResponse.builder()
                                    .status(e.getStatus().value())
                                    .code(e.getCode())
                                    .message(e.getMessage())
                                    .build();
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(
        FeignException.class
    )
    public ResponseEntity<Object> handleFeignException(FeignException e) {
        LOGGER.error("error", e);
    
        int httpStatus = e.status(); // FeignException에서 HTTP 상태 코드 가져오기
    
        if (httpStatus == HttpStatus.NOT_FOUND.value()) {
            ApiResponse response = ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .code("90002")
                    .message("Invalid credentials")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
             ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("99999")
                .message("Server error")
                .build();
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(
        MethodNotAllowedException.class
    )
    public ResponseEntity<Object> handleMethodNotAllowd(MethodNotAllowedException e){
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }
    

    @ExceptionHandler(
        RuntimeException.class
    )
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        LOGGER.error("error", e);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("99999")
                .message("Server error")
                .build();
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    };    

    @ExceptionHandler(
        MissingRequestHeaderException.class
    )
    public ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException e){
        LOGGER.error("error", e);
        return new ResponseEntity<>(
            ApiResponse.builder()
                .status(e.getStatusCode().value())
                .code("90001")
                .message(e.getMessage())
                .build(),
            e.getStatusCode()
        );
    }

    @ExceptionHandler(
        ExpiredJwtException.class
    )
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e){
        LOGGER.error("error", e);
        return new ResponseEntity<>(
            ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("90003")
                .message("Token Expired")
                .build(),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(
        JwtException.class
    )
    public ResponseEntity<Object> handleJwtException(JwtException e){
        return new ResponseEntity<>(
            ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("90004")
                .message("Invalied Token")
                .build(),
            HttpStatus.BAD_REQUEST
        );
    }
}
