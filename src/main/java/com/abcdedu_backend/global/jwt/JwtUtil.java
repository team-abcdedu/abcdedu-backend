package com.abcdedu_backend.global.jwt;

import com.abcdedu_backend.exception.ApplicationException;
import com.abcdedu_backend.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String MEMBER_ID_KEY = "memberId";
    private static final String IS_ACCESS_TOKEN = "isAccessToken";

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
        return Jwts.builder()
                .claim(MEMBER_ID_KEY, memberId)
                .claim(IS_ACCESS_TOKEN, true)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRED_MS))
                .signWith(accessTokenSecretKey)
                .compact();
    }

    public String createRefreshToken(Long memberId){
        return Jwts.builder()
                .claim(MEMBER_ID_KEY, memberId)
                .claim(IS_ACCESS_TOKEN, false)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXPIRED_MS))
                .signWith(refreshTokenSecretKey)
                .compact();
    }

    public Long getMemberIdFromRefreshToken(String rawToken) {
        try {
            Claims claims = extractClaims(rawToken, refreshTokenSecretKey);
            Boolean isAccessToken = claims.get(IS_ACCESS_TOKEN, Boolean.class);
            if (isAccessToken){
                throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
            if(claims.getExpiration().before(new Date())){
                throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
            return claims.get(MEMBER_ID_KEY, Long.class);
        } catch (Exception e){
            throw new ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public Long getMemberIdFromAccessToken(String rawToken) {
        try {
            Claims claims = extractClaims(rawToken, accessTokenSecretKey);
            Boolean isAccessToken = claims.get(IS_ACCESS_TOKEN, Boolean.class);
            if (!isAccessToken){
                throw new ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN);
            }
            if(claims.getExpiration().before(new Date())){
                throw new ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN);
            }
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

}
