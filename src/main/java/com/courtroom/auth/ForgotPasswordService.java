package com.courtroom.auth;
import com.courtroom.auth.strategy.ForgotPasswordStrategy;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordService {

    private final ForgotPasswordStrategy strategy;

    public ForgotPasswordService(ForgotPasswordStrategy strategy) {
        this.strategy = strategy;
    }

    public void forgotPassword(String username) {
        strategy.forgotPassword(username);
    }

    public void resetPassword(String token, String newPassword) {
        strategy.resetPassword(token, newPassword);
    }
}
