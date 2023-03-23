package com.example.test.oauth;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtManager jwtManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 로드
        HttpServletRequest requestServlet = (HttpServletRequest) request;
        String authorization = requestServlet.getHeader("Authorization");

        // 유효한지 확인
        if(StringUtils.isEmpty(authorization) == false) {
            String accessToken = authorization.replace("Bearer ", "");
            String name = jwtManager.getName(accessToken);

            // username이 나오지 않는다면 잘못된 토큰이 만들어진 것이므로 에러를 리턴한다
            if(StringUtils.isEmpty(name) == true) {
                // error
                throw new InvalidParameterException("유효하지 않은 토큰입니다");
            }

            // 여기서는 외부 소셜로그인을 통해 가입한 것이기 때문에 비밀번호 관리를 하지 않지만,
            // 만약 자체 로그인 시스템이 있다면 DB조회하여 비밀번호를 넣어주어야 한다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    name,
                    "password"
                    , getAuthorities());

            // 접근 허가
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // 권한을 설정하는 곳인데, 추가하고 싶다면 다음 2가지중 하나를 해야한다
    // 1) username을 기반으로 DB에 조회하여 ROLE_ 을 확인한다
    // 2) token을 만들때 ROLE_을 넣는다.
    private Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return grantedAuthorityList;
    }
}