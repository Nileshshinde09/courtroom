package com.courtroom.auth;

import com.courtroom.exception.*;
import com.courtroom.models.user.UserSession;
import com.courtroom.models.user.UserSessionRepository;
import com.courtroom.security.CustomUserDetails;
import com.courtroom.security.JwtService;
import com.courtroom.utils.UserUtils;
import com.courtroom.auth.dto.*;
import com.courtroom.config.SecurityConfig;
import com.courtroom.models.user.User;
import com.courtroom.models.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserUtils userUtils;
    private final SecurityConfig securityConfig;
    private final UserSessionRepository userSessionRepository;
    private final JwtService jwtService;
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email is already registered: " + request.email());
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username is already taken: " + request.username());
        }

        User user = User.builder()
                .displayName(request.displayName())
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getDisplayName(), saved.getUsername(), saved.getEmail());
    }

    @Transactional
    public LoginResponse loginResponse(LoginRequest request) {

        User user;

        // Login with email
        if (userUtils.isEmail(request.identifier())) {

            if (!userUtils.isValidEmail(request.identifier())) {
                throw new InvalidEmailException("Invalid email.");
            }

            user = userRepository.findByEmail(request.identifier())
                    .orElseThrow(() ->
                            new UserNotFoundException("User not found."));

        }
        // Login with username
        else {

            if (!userUtils.isValidUsername(request.identifier())) {
                throw new InvalidUsernameException("Invalid username.");
            }

            user = userRepository.findByUsername(request.identifier())
                    .orElseThrow(() ->
                            new UserNotFoundException("User not found."));
        }

        // Verify password
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Invalid password.");
        }

        // Create CustomUserDetails
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .role("ROLE_USER")
                .build();

        // Generate JWTs
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Create UserSession
        UserSession session = UserSession.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        userSessionRepository.save(session);

        return new LoginResponse(
                true,
                "User logged in successfully.",
                accessToken,
                refreshToken,
                new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getDisplayName(),
                        user.getEmail()
                )
        );
    }

    @Transactional
    public IsUsernameExistResponse isUsernameExistResponse(IsUsernameExistRequest request){
        if(userRepository.existsByUsername(request.username())){
            return new IsUsernameExistResponse(true,"Username is already taken");
        }
        return new IsUsernameExistResponse(false,"Username is unique");
    }

    @Transactional
    public StoreRefreshAndAccessTokenResponse storeRefreshAndAccessToken(
            String refreshToken,
            String accessToken,
            String id) {

        try {
            int updatedTokens = userSessionRepository.updateTokens(id, accessToken, refreshToken);

            if (updatedTokens == 0) {
                throw new UserNotFoundException("User not found.");
            }

            return new StoreRefreshAndAccessTokenResponse(
                    true,
                    "Access token and refresh token stored successfully."
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to store tokens.", e);
        }
    }

    @Transactional
    public StoreRefreshOrAccessTokenResponse storeAccessToken(String token,String id){
        try {
            int updatedToken = userSessionRepository.updateAccessToken(id, token);

            if (updatedToken == 0) {
                throw new UserNotFoundException("User not found.");
            }

            return new StoreRefreshOrAccessTokenResponse(
                    true,
                    "Access token token stored successfully."
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to store token.", e);
        }
    }
    @Transactional
    public StoreRefreshOrAccessTokenResponse storeRefreshToken(String token,String id){
        try {
            int updatedToken = userSessionRepository.updateRefreshToken(id, token);

            if (updatedToken == 0) {
                throw new UserNotFoundException("User not found.");
            }

            return new StoreRefreshOrAccessTokenResponse(
                    true,
                    "Refresh token stored successfully."
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to store token.", e);
        }
    }

    @Transactional
    public ClearAccessOrRefreshTokenResponse clearRefreshToken(String token,String id){
        try {
            int updatedToken = userSessionRepository.clearRefreshToken(id);

            if (updatedToken == 0) {
                throw new UserNotFoundException("User not found.");
            }

            return new ClearAccessOrRefreshTokenResponse(
                    true,
                    "Refresh Token Cleared Successfully."
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to clear token.", e);
        }
    }

    @Transactional
    public ClearAccessOrRefreshTokenResponse clearAccessToken(String token,String id){
        try {
            int updatedToken = userSessionRepository.clearAccessToken(id);

            if (updatedToken == 0) {
                throw new UserNotFoundException("User not found.");
            }

            return new ClearAccessOrRefreshTokenResponse(
                    true,
                    "Access Token Cleared Successfully."
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to clear token.", e);
        }
    }

    @Transactional
    public ClearAccessAndRefreshTokenResponse clearAccessAndRefreshToken(String token,String id){
        try {
            int updatedToken = userSessionRepository.clearTokens(id);

            if (updatedToken == 0) {
                throw new UserNotFoundException("User not found.");
            }

            return new ClearAccessAndRefreshTokenResponse(
                    true,
                    "Access & Refresh Token Cleared Successfully."
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to clear tokens.", e);
        }
    }

}
