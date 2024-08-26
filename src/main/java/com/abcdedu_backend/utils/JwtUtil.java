package com.abcdedu_backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {

    private Long ACCESS_TOKEN_EXPIRED_MS = Duration.ofHours(2).toMillis();
    private Long REFRESH_TOKEN_EXPIRED_MS = Duration.ofDays(14).toMillis();
    private final String USER_ID_KEY = "userId";

    @Value("${jwt.access-token-secret-key}")
    private String accessTokenSecretKey;
    @Value("${jwt.refresh-token-secret-key}")
    private String refreshTokenSecretKey;

    public String createAccessToken(Long userId){
        return createToken(userId, accessTokenSecretKey, ACCESS_TOKEN_EXPIRED_MS);
    }

    public String createRefreshToken(Long userId){
        return createToken(userId, refreshTokenSecretKey, REFRESH_TOKEN_EXPIRED_MS);
    }

    private String createToken(Long userId, String secretKey, Long expiredMS){
        return Jwts.builder()
                .claim(USER_ID_KEY, userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiredMS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getUserIdFromRefreshToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(refreshTokenSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get(USER_ID_KEY, Long.class);
    }
}
