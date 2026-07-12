package com.courtroom.exception;

public class JWTTokenExpiredException extends RuntimeException {
    public JWTTokenExpiredException(String message) {
        super(message);
    }
}
