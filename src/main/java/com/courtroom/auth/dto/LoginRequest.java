package com.courtroom.auth.dto;

public record LoginRequest(
        String identifier,
        String password,
        String email
) {
}