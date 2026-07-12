package com.courtroom.security;


import com.courtroom.exception.JWTTokenExpiredException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(7);


        if(!jwtService.isAccessTokenExpired(accessToken)) {
            throw new JWTTokenExpiredException("Access token has expired.");
        }else if(!jwtService.isAccessTokenValid(accessToken)){
            throw new JWTTokenExpiredException("Access token is Invalid.");
        }
        Claims claims = jwtService.extractClaims(accessToken);


        // TODO:
        // Validate JWT
        // Extract username
        // Load user
        // Set Authentication in SecurityContext

        filterChain.doFilter(request, response);
    }
}
