package com.teamproject.meeting.infrastructure.security.oauth;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.teamproject.meeting.infrastructure.redis.RedisUtil;
import com.teamproject.meeting.infrastructure.security.common.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil, RedisUtil redisUtil) {

        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String email = customUserDetails.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = jwtUtil.createJwt("access", email, role, 10 * 60 * 1000L); // 10분
        String refreshToken = jwtUtil.createJwt("refresh", email, role, 24 * 60 * 60 * 1000L); // 1일

        String uuid = java.util.UUID.randomUUID().toString();
        redisUtil.saveRefreshToken(email, uuid, refreshToken, 86_400_000L);
        
        // Access Token -> Header
        response.setHeader("Authorization", "Bearer " + accessToken);

        // Refresh Token -> HttpOnly Cookie
        response.addCookie(createCookie("refresh", refreshToken, uuid));

        // 로그인 성공 후 리다이렉트
        String redirectUrl = "http://localhost:3000/login-success?access=" + accessToken;
        response.sendRedirect(redirectUrl);
    }

    private Cookie createCookie(String key, String value, String uuid) {

        Cookie cookie = new Cookie(key, value + ":" + uuid);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}