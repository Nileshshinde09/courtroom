package com.courtroom.utils;

public class UserUtils {
    public boolean isEmail(String identifier) {
        return identifier != null &&
                identifier.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    public boolean isValidEmail(String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        return email.matches(emailRegex);
    }
    public boolean isValidUsername(String username) {

        if (username == null || username.isBlank()) {
            return false;
        }

        String usernameRegex = "^[a-zA-Z0-9._]{3,20}$";

        return username.matches(usernameRegex);
    }
}
