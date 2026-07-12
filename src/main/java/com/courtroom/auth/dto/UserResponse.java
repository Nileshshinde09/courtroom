package com.courtroom.auth.dto;

public record UserResponse(
        String id,
        String displayName,
        String username,
        String email
) {}
