package com.example.test.domain;

import com.example.test.dto.TokenDto;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ResponseToken {
    @NonNull
    private String accessToken;
    @NonNull
    private String refreshToken;
    @NonNull
    private Long expireSec;

    public ResponseToken(TokenDto tokenDto) {
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
        this.expireSec = tokenDto.getExpireSec();
    }
}