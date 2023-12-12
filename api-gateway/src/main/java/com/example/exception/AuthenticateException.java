package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

@Getter 
public class AuthenticateException extends ResponseStatusException {
    private String code;
    private String message;

    public AuthenticateException(HttpStatus status) {
        super(status);
    }

    public AuthenticateException(HttpStatus status, String message) {
        super(status, message);
    }

    public AuthenticateException(HttpStatus status, String code, String message) {
        super(status);
        this.code = code;
        this.message = message;
    }

    public AuthenticateException(HttpStatus status, String message, Throwable cause) {
        super(status, message, cause);
    }
}
