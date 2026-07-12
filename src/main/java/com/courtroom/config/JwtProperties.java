package com.courtroom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String accessTokenSecret;
    private String refreshTokenSecret;

    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}