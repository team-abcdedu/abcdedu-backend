package com.abcdedu_backend.global.jwt;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String MEMBER_ID_KEY = "memberId";
    private static final String ROLE_KEY = "role";

    private final Long ACCESS_TOKEN_EXPIRED_MS;
    private final Long REFRESH_TOKEN_EXPIRED_MS;

    private final SecretKey accessTokenSecretKey;
    private final SecretKey refreshTokenSecretKey;

    public JwtUtil(
            @Value("${jwt.access-token-secret-key}") String accessTokenSecretKey,
            @Value("${jwt.refresh-token-secret-key}") String refreshTokenSecretKey,
            //30분 -> 30*60*1000=1800000
            @Value("${jwt.access-token-expired-ms:1800000}") Long accessTokenExpiredMs,
            //14일 -> 14*24*60*60*1000=1209600000
            @Value("${jwt.refresh-token-expired-ms:1209600000}") Long refreshTokenExpiredMs
    ){
        this.accessTokenSecretKey = Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes());
        this.refreshTokenSecretKey = Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes());
        this.ACCESS_TOKEN_EXPIRED_MS = accessTokenExpiredMs;
        this.REFRESH_TOKEN_EXPIRED_MS = refreshTokenExpiredMs;
    }



    public String createAccessToken(Long memberId){
        return createToken(memberId, accessTokenSecretKey, ACCESS_TOKEN_EXPIRED_MS);
    }

    public String createRefreshToken(Long memberId){
        return createToken(memberId, refreshTokenSecretKey, REFRESH_TOKEN_EXPIRED_MS);
    }

    private String createToken(Long memberId, Key secretKey, Long expiredMS){
        return Jwts.builder()
                .claim(MEMBER_ID_KEY, memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiredMS))
                .signWith(secretKey)
                .compact();
    }

    public Long getMemberIdFromRefreshToken(String token) {
        try {
            Claims claims = extractClaims(token, refreshTokenSecretKey);
            return claims.get(MEMBER_ID_KEY, Long.class);
        } catch (Exception e){
            throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public Long getMemberIdFromAccessToken(String token) {
        token = removeBearerPrefix(token);
        try {
            Claims claims = extractClaims(token, accessTokenSecretKey);
            return claims.get(MEMBER_ID_KEY, Long.class);
        } catch (Exception e){
            throw new ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

    }

    private Claims extractClaims(String token, SecretKey secretKey){
        return Jwts.parser()
            .verifyWith(secretKey).build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private String removeBearerPrefix(String token){
        if (!token.startsWith("Bearer ")){
            throw new ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        token = token.substring(7);
        return token;
    }
}
