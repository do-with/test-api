package com.example.test.service;

import com.example.test.dto.TokenDto;
import com.example.test.oauth.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtManager jwtManager;

    // 토큰 발급
    public TokenDto createToken(String key) {
        String accessToken = jwtManager.generateAccessToken(key);
        String refreshToken = jwtManager.generateRefreshToken(key);
        return new TokenDto(
                accessToken
                , refreshToken
                , jwtManager.getValidationAccessTokenTime());
    }

    // refresh 토큰 갱신
    public TokenDto createTokenByrefreshToken(String refreshToken) {
        String key = jwtManager.getName(refreshToken);
        String accessToken = jwtManager.generateAccessToken(key);
        String refreshTokenNew = jwtManager.generateRefreshToken(key);
        return new TokenDto(
                accessToken
                , refreshTokenNew
                , jwtManager.getValidationAccessTokenTime());
    }
}