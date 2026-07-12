package com.courtroom.auth.dto;

public record StoreRefreshAndAccessTokenResponse(
        boolean success,
        String message
) {
}