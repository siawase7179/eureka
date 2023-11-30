package com.example.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException{
    private HttpStatus status;
    private String code;

    public RestException(HttpStatus status, String code, String message){
        super(message);
        this.status = status;
        this.code = code;
    }
}
