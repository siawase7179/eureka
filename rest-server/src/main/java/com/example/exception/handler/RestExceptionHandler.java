package com.example.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import com.example.exception.RestException;
import com.example.vo.ApiResponse;

import feign.FeignException;

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

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException e) {
        LOGGER.error("error", e);
    
        int httpStatus = e.status(); // FeignException에서 HTTP 상태 코드 가져오기
    
        if (httpStatus == HttpStatus.NOT_FOUND.value()) {
            ApiResponse response = ApiResponse.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .code("90003")
                    .message("id/pass not found")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (httpStatus == HttpStatus.UNAUTHORIZED.value()) {
            ApiResponse response = ApiResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .code("90004")
                    .message("Token Error")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else {
             ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("99999")
                .message("Server error")
                .build();
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<Object> handleMethodNotAllowd(MethodNotAllowedException e){
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }
    

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        LOGGER.error("error", e);
        ApiResponse response = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code("99999")
                .message("Server error")
                .build();
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    };    
}
