package com.example.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/**"))
                .permitAll()
                .and()
                .oauth2Login();

        return http.build();
    }
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http
////                .csrf().disable();
//                .oauth2Login()
//                .and()
//                .authorizeHttpRequests().requestMatchers(
//                    new AntPathRequestMatcher("/**")).permitAll();
////            .and()
////                .csrf().ignoringRequestMatchers(
////                        new AntPathRequestMatcher("/h2-console/**"))
////            .and()
////                .headers()
////                .addHeaderWriter(new XFrameOptionsHeaderWriter(
////                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
//
//        return http.build();
//    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}