package com.example.test.service;

import com.example.test.domain.AuthorizationKakao;
import com.example.test.domain.ResponseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Oauth2Kakao oauth2Kakao;
    private final JwtService jwtService;

    // 카카오로 인증받기
    public void oauth2AuthorizationKakao(String code) {
        AuthorizationKakao authorization = oauth2Kakao.callTokenApi(code);
        String userInfoFromKakao = oauth2Kakao.callGetUserByAccessToken(authorization.getAccess_token());
        System.out.println("userInfoFromKakao = " + userInfoFromKakao);
    }

    // 로그인 & 토큰발행
    public ResponseToken login(Long userId) {
        return new ResponseToken(jwtService.createToken(userId.toString()));
    }
}