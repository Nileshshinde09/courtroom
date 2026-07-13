package com.courtroom.auth.dto;

public record ClearAccessOrRefreshTokenResponse(
        boolean success,
        String message
) {
}