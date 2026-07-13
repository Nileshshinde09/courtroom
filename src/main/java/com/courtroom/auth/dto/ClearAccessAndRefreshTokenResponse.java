package com.courtroom.auth.dto;

public record ClearAccessAndRefreshTokenResponse(
        boolean success,
        String message
) {
}