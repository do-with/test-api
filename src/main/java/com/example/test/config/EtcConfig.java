package com.example.test.config;

import com.example.test.oauth.JwtManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EtcConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public JwtManager jwtManager() {
        String secretKey = "YhnNm27TkBp8tWYfAYJWEUKCAJCqUVXS";
        Long accessTokenExpireSecond = (long) (60 * 60 * 24); // 1일
        Long refreshTokenExpireSecond = (long) (60 * 60 * 24 * 30); // 1개월
        return new JwtManager(secretKey, accessTokenExpireSecond, refreshTokenExpireSecond);
    }
}