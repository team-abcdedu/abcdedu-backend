package com.abcdedu_backend.utils;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {

    private Long ACCESS_TOKEN_EXPIRED_MS = Duration.ofMinutes(30).toMillis();
    private Long REFRESH_TOKEN_EXPIRED_MS = Duration.ofDays(14).toMillis();
    private static final String MEMBER_ID_KEY = "memberId";

    @Value("${jwt.access-token-secret-key}")
    private String accessTokenSecretKey;
    @Value("${jwt.refresh-token-secret-key}")
    private String refreshTokenSecretKey;

    public String createAccessToken(Long memberId){
        return createToken(memberId, accessTokenSecretKey, ACCESS_TOKEN_EXPIRED_MS);
    }

    public String createRefreshToken(Long memberId){
        return createToken(memberId, refreshTokenSecretKey, REFRESH_TOKEN_EXPIRED_MS);
    }

    private String createToken(Long memberId, String secretKey, Long expiredMS){
        return Jwts.builder()
                .claim(MEMBER_ID_KEY, memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiredMS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long getMemberIdFromRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(refreshTokenSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(MEMBER_ID_KEY, Long.class);
        } catch (Exception e){
            throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public Long getMemberIdFromAccessToken(String token) {
        token = removeBearerPrefix(token);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(accessTokenSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(MEMBER_ID_KEY, Long.class);
        } catch (Exception e){
            throw new ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

    }

    private String removeBearerPrefix(String token){
        if (!token.startsWith("Bearer ")){
            throw new ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        token = token.substring(7);
        return token;
    }
}
