package com.courtroom.auth.dto;

public record LoginResponse(
        boolean success,
        String message,
        String accessToken,
        String refreshToken,
        UserResponse user
) {
}