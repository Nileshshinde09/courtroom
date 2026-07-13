package com.courtroom.auth.strategy;

public interface ForgotPasswordStrategy {

    void forgotPassword(String username);

    void resetPassword(String token, String newPassword);
}