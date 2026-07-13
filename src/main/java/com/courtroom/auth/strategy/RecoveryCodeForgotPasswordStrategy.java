package com.courtroom.auth.strategy;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("recovery-code")
public class RecoveryCodeForgotPasswordStrategy implements ForgotPasswordStrategy {

    @Override
    public void forgotPassword(String username) {
        // Validate recovery code
        // Generate reset JWT
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // Verify JWT
        // Update password
    }
}