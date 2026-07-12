package com.courtroom.exception;

public class BadJWTTokenException extends RuntimeException {
    public BadJWTTokenException(String message) {
        super(message);
    }
}

