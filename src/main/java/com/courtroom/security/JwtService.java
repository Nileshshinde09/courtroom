package com.courtroom.security;

import com.courtroom.config.JwtProperties;
import com.courtroom.exception.BadJWTTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String generateAccessToken(CustomUserDetails user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("displayName", user.getDisplayName());
        claims.put("role", user.getRole());
        // TODO:
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()
                ))
                .signWith(Keys.hmacShaKeyFor(
                        jwtProperties.getAccessTokenSecret().getBytes(StandardCharsets.UTF_8)
                ))
                .compact();
    };

    public String generateRefreshToken(CustomUserDetails user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("displayName", user.getDisplayName());
        claims.put("role", user.getRole());
        //TODO:
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()
                ))
                .signWith(Keys.hmacShaKeyFor(
                        jwtProperties.getRefreshTokenSecret().getBytes(StandardCharsets.UTF_8)
                ))
                .compact();
    };

    public Claims extractClaims(String token){
        try {
             return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(
                            jwtProperties.getAccessTokenSecret().getBytes(StandardCharsets.UTF_8)
                    ))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadJWTTokenException("Invalid JWT token or Invalid signature");
        }
    };

    public boolean isAccessTokenValid(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(
                            jwtProperties.getAccessTokenSecret().getBytes(StandardCharsets.UTF_8)
                    ))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();

            return !username.isEmpty()
                    && claims.getExpiration().after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            if(isRefreshTokenExpired(token)){
                throw new BadJWTTokenException("Refresh Token is Expired");
            }else if(isRefreshTokenValid(token)){
                throw new BadJWTTokenException("Refresh Token is Invalid");
            }


        }
    }
    public boolean isRefreshTokenValid(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(
                            jwtProperties.getRefreshTokenSecret().getBytes(StandardCharsets.UTF_8)
                    ))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();

            return !username.isEmpty() && claims.getExpiration().after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isAccessTokenExpired(String token){
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(
                            jwtProperties.getAccessTokenSecret().getBytes(StandardCharsets.UTF_8)
                    ))
                    .build()
                    .parseSignedClaims(token);

            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw new BadJWTTokenException("Invalid JWT Access token or Invalid signature");
        }
    };

    public boolean refreshAccessToken(String token){
        try {
            Claims claims=Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(
                            jwtProperties.getRefreshTokenSecret().getBytes(StandardCharsets.UTF_8)
                    ))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            //TODO:


        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw new BadJWTTokenException("Invalid JWT Access token or Invalid signature");
        }
    };

    public boolean isRefreshTokenExpired(String token){
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(
                            jwtProperties.getAccessTokenSecret().getBytes(StandardCharsets.UTF_8)
                    ))
                    .build()
                    .parseSignedClaims(token);

            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw new BadJWTTokenException("Invalid JWT Refresh token or Invalid signature");
        }
    };
}
