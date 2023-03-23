package com.example.test.dto;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class TokenDto {
    @NonNull
    private String accessToken;
    @NonNull
    private String refreshToken;
    @NonNull
    private Long expireSec; // 만료시간(second)

    public TokenDto(@NonNull String accessToken, @NonNull String refreshToken,
                    @NonNull Long expireSec) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireSec = expireSec;
    }
}