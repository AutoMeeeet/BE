package com.teamproject.meeting.infrastructure.security.common;

import java.io.IOException;

import org.springframework.web.filter.GenericFilterBean;

import com.teamproject.meeting.infrastructure.redis.RedisUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    public CustomLogoutFilter(JWTUtil jwtUtil, RedisUtil redisUtil) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // /logout + POST 아닐 경우 계속 next filter
        if (!request.getRequestURI().equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!"POST".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Refresh Token + UUID 읽기
        String refresh = null;
        String uuid = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    String[] parts = cookie.getValue().split(":");
                    if (parts.length == 2) {
                        refresh = parts[0];
                        uuid = parts[1];
                    }
                    break;
                }
            }
        }

        // 토큰이 없으면 BAD REQUEST
        if (refresh == null || uuid == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 만료 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // refresh 카테고리인지 확인
        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Redis 저장 여부 확인
        String email = jwtUtil.getEmail(refresh);

        if (!redisUtil.hasRefreshToken(email, uuid)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 저장된 토큰과 일치하는지 확인 (보안 강화)
        String storedToken = redisUtil.getRefreshToken(email, uuid);
        if (!refresh.equals(storedToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 로그아웃 처리 → Refresh Token 삭제
        redisUtil.deleteRefreshToken(email, uuid);

        // Refresh Cookie 제거
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
