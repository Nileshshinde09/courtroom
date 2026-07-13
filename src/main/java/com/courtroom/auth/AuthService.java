package com.courtroom.auth;

import com.courtroom.auth.dto.*;
import com.courtroom.exception.EmailAlreadyExistsException;
import com.courtroom.exception.UserNotFoundException;
import com.courtroom.exception.UsernameAlreadyExistsException;
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
            int updatedTokens = userRepository.updateTokens(id, accessToken, refreshToken);

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
            int updatedToken = userRepository.updateAccessToken(id, token);

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
            int updatedToken = userRepository.updateRefreshToken(id, token);

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
            int updatedToken = userRepository.clearRefreshToken(id);

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
            int updatedToken = userRepository.clearAccessToken(id);

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
            int updatedToken = userRepository.clearTokens(id);

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
