package com.example.test.config;

import com.example.test.oauth.CustomAuthenticationEntryPoint;
import com.example.test.oauth.JwtAuthenticationFilter;
import com.example.test.oauth.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig {

    private final JwtManager jwtManager;

    @Bean
    SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()// rest api이므로 csrf 보안이 필요없으므로 비활성화
            .httpBasic().disable() // rest api 이므로 로그인폼으로 이동 비활성화
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션필요없음
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtManager) // jwt 로 접근허용 필터 생성
                    , UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()  // exception이 날 경우 catch하여 result form 일관하게 만듬(필요없음 제거)
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;
        return http.build();
    }

    private String[] whitelistedUriPatterns() {
        return new String[]{
                "/health",
                "/oauth2/authorization/**",
                "/swagger*/**", "/webjars*/**", "/v2/api-docs",
                "/user/login/**",
        };
    }
}