package com.teamproject.meeting.infrastructure.security.common;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.teamproject.meeting.infrastructure.redis.RedisUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ReissueService {

	private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    public ReissueService(JWTUtil jwtUtil, RedisUtil redisUtil) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    
    // 요청에서 Refresh Token과 UUID 추출
    public String[] extractRefreshTokenWithUUID(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh".equals(cookie.getName()))
                .map(cookie -> {
                    String value = cookie.getValue();
                    String[] parts = value.split(":");
                    if (parts.length == 2) {
                        return new String[]{parts[0], parts[1]}; // [token, uuid]
                    }
                    return null;
                })
                .filter(parts -> parts != null) // null이 아닌
                .findFirst() // 첫번째
                .orElse(null);
    }

    // Refresh Token 검증
    public boolean validateRefreshToken(String refreshToken, String uuid) {
    	if (refreshToken == null || uuid == null) return false;

        // 토큰 만료 체크
        if (jwtUtil.isExpired(refreshToken)) return false;

        // 카테고리 체크
        String category = jwtUtil.getCategory(refreshToken);
        if (!"refresh".equals(category)) return false;
        
        // Redis에 해당 토큰이 존재하는지 확인
        String email = jwtUtil.getEmail(refreshToken);

        if (!redisUtil.hasRefreshToken(email, uuid)) return false;
        
        // Redis에 저장된 토큰과 요청된 토큰이 일치하는지 확인
        String storedToken = redisUtil.getRefreshToken(email, uuid);
        return refreshToken.equals(storedToken);
    }

    // 새로운 토큰 발급 및 Redis 갱신
    public String[] reissueTokens(String oldRefreshToken, String oldUuid) {
        String email = jwtUtil.getEmail(oldRefreshToken);
        String role = jwtUtil.getRole(oldRefreshToken);

        // 새로운 Access Token 발급
        String newAccess = jwtUtil.createJwt("access", email, role, 600_000L); // 10분
        
        // 새로운 Refresh Token 발급
        String newRefresh = jwtUtil.createJwt("refresh", email, role, 86_400_000L); // 1일
        String newUuid = java.util.UUID.randomUUID().toString();
        
        // Redis에서 기존 Refresh Token 삭제
        redisUtil.deleteRefreshToken(email, oldUuid);
        
        // Redis에 새로운 Refresh Token 저장
        redisUtil.saveRefreshToken(email, newUuid, newRefresh, 86_400_000L);
        
        return new String[]{newAccess, newRefresh, newUuid};
    }
}
