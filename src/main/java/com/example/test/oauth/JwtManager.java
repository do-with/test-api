package com.example.test.oauth;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidParameterException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


public class JwtManager {
    // 30분
    private long ACCESS_TOKEN_VALIDATiON_SECOND = 60 * 30;
    // 1개월
    private long REFRESH_TOKEN_VALIDATiON_SECOND = 60 * 60 * 24 * 30;


    private String secretKey;

    public JwtManager(String secretKey) {
        this.secretKey = secretKey;
    }
    public JwtManager(
            String secretKey
            , Long accessTokenValidationSecond
            , Long refreshTokenValidationSecond
    ) {
        this.secretKey = secretKey;
        this.ACCESS_TOKEN_VALIDATiON_SECOND = accessTokenValidationSecond;
        this.REFRESH_TOKEN_VALIDATiON_SECOND = refreshTokenValidationSecond;
    }

//    private Key getSigninKey(String secretKey) {
//        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
    private Key getSigninKey(String secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HMACSHA256");
    }

    // 토큰 해석
    public Claims validTokenAndReturnBody(String token) {
        try {
//            return Jwts.parserBuilder()
            return Jwts.parser()
                    .setSigningKey(getSigninKey(secretKey))
//                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new InvalidParameterException("유효하지 않은 토큰입니다");
        }
    }

    // 유저id 조회
    public String getName(String token) {
        return validTokenAndReturnBody(token).get("username", String.class);
    }

    // 토큰 만료
    private Boolean isTokenExpired(String token){
        Date expiration = validTokenAndReturnBody(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateAccessToken(String username) {
        return doGenerateToken(username, ACCESS_TOKEN_VALIDATiON_SECOND * 1000l);
    }

    public String generateRefreshToken(String username) {
//        return doGenerateToken("refresh-" + username, REFRESH_TOKEN_VALIDATiON_SECOND * 1000l);
        return doGenerateToken(username, REFRESH_TOKEN_VALIDATiON_SECOND * 1000l);
    }

    public Long getValidationAccessTokenTime(){
        return ACCESS_TOKEN_VALIDATiON_SECOND;
    }

    private String doGenerateToken(String username, Long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
//                .signWith(getSigninKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

}