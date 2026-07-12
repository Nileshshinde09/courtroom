package com.courtroom.auth;

import com.courtroom.auth.dto.IsUsernameExistRequest;
import com.courtroom.auth.dto.IsUsernameExistResponse;
import com.courtroom.auth.dto.RegisterRequest;
import com.courtroom.auth.dto.UserResponse;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/is-username-exist/{username}")
    public ResponseEntity<IsUsernameExistResponse> isUsernameExist(
            @PathVariable String username) {

        IsUsernameExistRequest request = new IsUsernameExistRequest(username);
        IsUsernameExistResponse response = authService.isUsernameExistResponse(request);

        return ResponseEntity.ok(response);
    }

}