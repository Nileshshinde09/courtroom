package com.courtroom.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IsUsernameExistRequest(@NotBlank(message = "Username is required")
                                     @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
                                     String username) {

}
